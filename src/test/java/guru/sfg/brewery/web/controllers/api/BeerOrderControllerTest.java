package guru.sfg.brewery.web.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.bootstrap.DefaultBreweryLoader;
import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.Customer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerOrderDto;
import guru.sfg.brewery.web.model.BeerOrderLineDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maarten Casteels
 */
@SpringBootTest
class BeerOrderControllerTest extends BaseIT {

    public static final String API_ROOT = "/api/v1/customers/";

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    Customer stPeteCustomer;
    Customer dunedinCustomer;
    Customer keyWestCustomer;
    List<Beer> loadedBeers;

    @BeforeEach
    void setUp() {
        stPeteCustomer = customerRepository.findByCustomerNameLike(DefaultBreweryLoader.ST_PETE_DISTRIBUTING).orElseThrow();
        dunedinCustomer = customerRepository.findByCustomerNameLike(DefaultBreweryLoader.DUNEDIN_DISTRIBUTING).orElseThrow();
        keyWestCustomer = customerRepository.findByCustomerNameLike(DefaultBreweryLoader.KEY_WEST_DISTRIBUTORS).orElseThrow();
        loadedBeers = beerRepository.findAll();
    }

    private BeerOrderDto buildOrderDto(Customer customer, UUID beerId) {
        List<BeerOrderLineDto> orderLines = List.of(BeerOrderLineDto.builder()
                .id(UUID.randomUUID())
                .beerId(beerId)
                .orderQuantity(5)
                .build());

        return BeerOrderDto.builder()
                .customerId(customer.getId())
                .customerRef("123")
                .orderStatusCallbackUrl("http://example.com")
                .beerOrderLines(orderLines)
                .build();
    }

    @DisplayName("Create Order Test")
    @Nested
    class createOrderTests {

        @Test
        void createOrderGuest() throws Exception {
            BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

            mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerOrderDto)))
                    .andExpect(status().isUnauthorized());
        }

        @WithUserDetails("spring")
        @Test
        void createOrderUserAdmin() throws Exception {
            BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

            mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerOrderDto)))
                    .andExpect(status().isCreated());
        }

        @WithUserDetails(DefaultBreweryLoader.ST_PETE_USER)
        @Test
        void createOrderUserAuthCustomer() throws Exception {
            BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

            mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerOrderDto)))
                    .andExpect(status().isCreated());
        }

        @WithUserDetails(DefaultBreweryLoader.KEY_WEST_USER)
        @Test
        void createOrderUserNotAuthCustomer() throws Exception {
            BeerOrderDto beerOrderDto = buildOrderDto(stPeteCustomer, loadedBeers.get(0).getId());

            mockMvc.perform(post(API_ROOT + stPeteCustomer.getId() + "/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8.name())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerOrderDto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Transactional
    @DisplayName("Get Order By ID")
    @Nested
    class GetOrderByIdTests {

        @Test
        void listOrdersAsGuest() throws Exception {
            final BeerOrder order = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();

            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders/" + order.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @WithUserDetails("spring")
        @Test
        void listOrdersAsAdmin() throws Exception {
            final BeerOrder order = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();

            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders/" + order.getId()))
                    .andExpect(status().isOk());
        }

        @WithUserDetails(DefaultBreweryLoader.ST_PETE_USER)
        @Test
        void listOrdersAsCustomer() throws Exception {
            final BeerOrder order = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();

            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders/" + order.getId()))
                    .andExpect(status().isOk());
        }

        @WithUserDetails(DefaultBreweryLoader.DUNEDIN_USER)
        @Test
        void listOrdersAsNotRightCustomer() throws Exception {
            final BeerOrder order = stPeteCustomer.getBeerOrders().stream().findFirst().orElseThrow();

            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders/" + order.getId()))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("List Orders")
    @Nested
    class ListOrderTests {

        @Test
        void listOrdersAsGuest() throws Exception {
            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                    .andExpect(status().isUnauthorized());
        }

        @WithUserDetails("spring")
        @Test
        void listOrdersAsAdmin() throws Exception {
            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                    .andExpect(status().isOk());
        }

        @WithUserDetails(DefaultBreweryLoader.ST_PETE_USER)
        @Test
        void listOrdersAsCustomer() throws Exception {
            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                    .andExpect(status().isOk());
        }

        @WithUserDetails(DefaultBreweryLoader.DUNEDIN_USER)
        @Test
        void listOrdersAsNotRightCustomer() throws Exception {
            mockMvc.perform(get(API_ROOT + stPeteCustomer.getId() + "/orders"))
                    .andExpect(status().isForbidden());
        }
    }

    @DisplayName("Pick Up Orders")
    @Nested
    @Disabled
    class PickUpOrderTest {

        @Test
        void pickUpOrderAsGuest() throws Exception {
        }

        @Test
        void pickUpOrderAsCustomer() throws Exception {
        }

        @Test
        void pickUpOrderAsAdmin() throws Exception {
        }

        @Test
        void pickUpOrderAsWrongCustomer() throws Exception {
        }
    }
}

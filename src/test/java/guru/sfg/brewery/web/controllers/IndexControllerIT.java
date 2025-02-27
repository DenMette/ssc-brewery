package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerOrderService;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maarten Casteels
 */
@WebMvcTest
public class IndexControllerIT extends BaseIT {

    @MockBean
    protected BeerRepository beerRepository;

    @MockBean
    protected BeerInventoryRepository beerInventoryRepository;

    @MockBean
    protected CustomerRepository customerRepository;

    @MockBean
    protected BreweryService breweryService;

    @MockBean
    protected BeerService beerService;

    @MockBean
    protected BeerOrderService beerOrderService;

    @Test
    void testGetIndexSlash() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}

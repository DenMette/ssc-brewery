package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @DisplayName("Delete Test")
    @Nested
    class DeleteTests {
        public Beer beerToDelete() {
            Random random = new Random();

            return beerRepository.save(Beer.builder()
                    .beerName("To delete")
                    .beerStyle(BeerStyleEnum.IPA)
                    .upc(String.valueOf(random.nextInt(99_999_999)))
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build());
        }


        @Disabled("We don't use the headers anymore")
        @Test
        void deleteBeerWithBadCredentialsApi() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be")
                    .header("Api-Key", "spring")
                    .header("Api-Secret", "guruXXX"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerAsAdmin() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerAsUser() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerAsCustomer() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerAsGuest() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerWithBadCredentials() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("stupid", "me")))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Find Beers")
    @Nested
    class FindBeerTest {
        @Test
        void findBeers() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerById() throws Exception {
            final Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerAsAdmin() throws Exception {
            mockMvc.perform(get("/beers").param("beerName", "")
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().isOk());
        }
    }
}
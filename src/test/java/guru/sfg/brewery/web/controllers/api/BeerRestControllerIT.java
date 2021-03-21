package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
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

    @Nested
    @DisplayName("Find Beer Test")
    class FindBeerTest {
        @Test
        void findBeerNoAuth() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }
        @Test
        void findBeerUser() throws Exception {
            mockMvc.perform(get("/api/v1/beer/")
            .with(httpBasic("user", "passowrd")))
                    .andExpect(status().isUnauthorized());
        }
        @Test
        void findBeerCustomer() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }
        @Test
        void findBeerAdmin() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void findBeerById() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Delete Beer Test")
    class DeleteBeerTest {
        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.save(Beer.builder()
                    .beerName("Delete My Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99_999_999)))
                    .build());
        }

        @Test
        void deleteBeerWithBadCredentials() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be")
                    .header("Api-Key", "spring")
                    .header("Api-Secret", "guruXXX"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be")
                    .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be")
                    .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuthorized() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
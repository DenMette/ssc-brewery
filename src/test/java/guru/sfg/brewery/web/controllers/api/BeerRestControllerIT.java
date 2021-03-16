package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be")
                .header("Api-Key", "spring")
                .header("Api-Secret", "guru"))
                .andExpect(status().isOk());
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
        mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be")
                .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/61f080b3-8cf0-428b-ac53-d446ab7215be"))
                .andExpect(status().isUnauthorized());
    }
}
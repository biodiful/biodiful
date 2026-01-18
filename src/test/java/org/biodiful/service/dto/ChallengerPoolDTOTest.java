package org.biodiful.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.biodiful.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChallengerPoolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChallengerPoolDTO.class);
        ChallengerPoolDTO challengerPoolDTO1 = new ChallengerPoolDTO();
        challengerPoolDTO1.setId(1L);
        ChallengerPoolDTO challengerPoolDTO2 = new ChallengerPoolDTO();
        assertThat(challengerPoolDTO1).isNotEqualTo(challengerPoolDTO2);
        challengerPoolDTO2.setId(challengerPoolDTO1.getId());
        assertThat(challengerPoolDTO1).isEqualTo(challengerPoolDTO2);
        challengerPoolDTO2.setId(2L);
        assertThat(challengerPoolDTO1).isNotEqualTo(challengerPoolDTO2);
        challengerPoolDTO1.setId(null);
        assertThat(challengerPoolDTO1).isNotEqualTo(challengerPoolDTO2);
    }
}

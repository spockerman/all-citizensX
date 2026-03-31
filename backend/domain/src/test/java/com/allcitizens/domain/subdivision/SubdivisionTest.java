package com.allcitizens.domain.subdivision;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubdivisionTest {

    @Test
    void shouldCreateValidSubdivision() {
        var subdivision = Subdivision.create("Urban Planning");

        assertThat(subdivision.getId()).isNotNull();
        assertThat(subdivision.getName()).isEqualTo("Urban Planning");
        assertThat(subdivision.isActive()).isTrue();
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        assertThatThrownBy(() -> Subdivision.create(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        assertThatThrownBy(() -> Subdivision.create(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldActivate() {
        var subdivision = Subdivision.create("Urban Planning");
        subdivision.deactivate();

        subdivision.activate();

        assertThat(subdivision.isActive()).isTrue();
    }

    @Test
    void shouldDeactivate() {
        var subdivision = Subdivision.create("Urban Planning");

        subdivision.deactivate();

        assertThat(subdivision.isActive()).isFalse();
    }

    @Test
    void shouldUpdateSubdivision() {
        var subdivision = Subdivision.create("Old Name");

        subdivision.update("New Name");

        assertThat(subdivision.getName()).isEqualTo("New Name");
    }

    @Test
    void shouldFailUpdateWithBlankName() {
        var subdivision = Subdivision.create("Urban Planning");

        assertThatThrownBy(() -> subdivision.update(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name must not be blank");
    }

    @Test
    void shouldReconstitute() {
        var id = UUID.randomUUID();

        var subdivision = Subdivision.reconstitute(id, "Stored", false);

        assertThat(subdivision.getId()).isEqualTo(id);
        assertThat(subdivision.getName()).isEqualTo("Stored");
        assertThat(subdivision.isActive()).isFalse();
    }
}

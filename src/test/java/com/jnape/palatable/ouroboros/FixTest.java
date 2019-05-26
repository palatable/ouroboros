package com.jnape.palatable.ouroboros;

import org.junit.Test;
import testsupport.NatF;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.ouroboros.Fix.fix;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static testsupport.NatF.s;
import static testsupport.NatF.z;

public class FixTest {

    @Test
    public void validTypeSafeSignatures() {
        @SuppressWarnings("unused") Fix<NatF<?>, ?>                 representableAssignment = Fix.<NatF<?>, NatF<? extends Fix<NatF<?>, ?>>>fix(s(fix(s(fix(z())))));
        @SuppressWarnings("unused") Fix<NatF<?>, ? extends NatF<?>> reprehensibleAssignment = fix(s(fix(s(fix(z())))));
    }

    @Test
    public void fixAndUnfix() {
        NatF<Fix<NatF<?>, ?>> z = z();
        assertEquals(z, fix(z).unfix());
    }

    @Test
    public void equalsAndHashCode() {
        assertEquals(fix(z()), fix(z()));
        assertEquals(fix(s(fix(z()))), fix(s(fix(z()))));
        assertNotEquals(fix(nothing()), fix(z()));
        assertNotEquals(fix(nothing()), new Object());

        assertEquals(fix(z()).hashCode(), fix(z()).hashCode());
        assertNotEquals(fix(s(fix(z()))).hashCode(), fix(z()).hashCode());
    }

    @Test
    public void serialForm() {
        assertEquals("fix(s(fix(z())))", fix(s(fix(z()))).toString());
    }
}
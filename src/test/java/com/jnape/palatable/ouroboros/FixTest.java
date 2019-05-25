package com.jnape.palatable.ouroboros;

import org.junit.Test;
import testsupport.NatF;

import static com.jnape.palatable.ouroboros.Fix.fix;
import static org.junit.Assert.assertEquals;
import static testsupport.NatF.s;
import static testsupport.NatF.z;

public class FixTest {

    @Test
    @SuppressWarnings("unused")
    public void validTypeSafeSignatures() {
        Fix<NatF<?>, NatF<Fix<NatF<?>, NatF<Fix<NatF<?>, NatF<Fix<NatF<?>, ?>>>>>>> lossless = fix(s(fix(s(fix(z())))));
        Fix<NatF<?>, NatF<Fix<NatF<?>, ?>>>                                         compact  = fix(s(fix(s(fix(z())))));
        Fix<NatF<?>, ?>                                                             minimal  = fix(s(fix(s(fix(z())))));
    }

    @Test
    public void fixAndUnfix() {
        NatF<Fix<NatF<?>, ?>> z = z();
        assertEquals(z, fix(z).unfix());
    }
}
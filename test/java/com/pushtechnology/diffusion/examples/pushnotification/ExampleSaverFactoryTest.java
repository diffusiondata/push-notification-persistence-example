/*******************************************************************************
 * Copyright (c) 2015 Push Technology Ltd., All Rights Reserved.
 *
 * Use is subject to license terms.
 *
 * NOTICE: All information contained herein is, and remains the
 * property of Push Technology. The intellectual and technical
 * concepts contained herein are proprietary to Push Technology and
 * may be covered by U.S. and Foreign Patents, patents in process, and
 * are protected by trade secret or copyright law.
 *******************************************************************************/
package com.pushtechnology.diffusion.examples.pushnotification;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.pushtechnology.diffusion.examples.pushnotification.ExampleSaverFactory;

public class ExampleSaverFactoryTest {
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void testBuildNoFile() throws Exception {
        final File file = new File(folder.getRoot(), "noSuchFile");

        new ExampleSaverFactory().build(file.getAbsolutePath());
    }

}

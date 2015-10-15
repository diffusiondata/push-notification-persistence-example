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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.pushtechnology.diffusion.examples.pushnotification.Model;
import com.pushtechnology.diffusion.examples.pushnotification.ModelCodec;

public class ModelCodecTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Model model;
    private File emptyFile;

    @Before
    public void setUp() throws Exception {
        emptyFile = tempFolder.newFile();

        model = new Model();
        model.addSubscription("some/topic", "foo://bar");
    }

    @Test
    public void testSaveThenLoad() throws Exception {
        new ModelCodec(emptyFile).save(model);

        assertThat(emptyFile.length(), not(0L));

        final Model loadedModel = new ModelCodec(emptyFile).load();

        assertThat(loadedModel.getState(), hasKey("some/topic"));
        assertThat(loadedModel.getState().get("some/topic"), contains("foo://bar"));
    }

    @Test(expected=RuntimeException.class)
    public void testSaveToBadDir() throws Exception {
        final File badFile = new File(new File(tempFolder.getRoot(), "some-non-existing-dir"), "some-file.ser");
        new ModelCodec(badFile).save(model);
    }

    @Test(expected=RuntimeException.class)
    public void testLoadBadFile() throws Exception {
        new ModelCodec(emptyFile).load();
    }

}

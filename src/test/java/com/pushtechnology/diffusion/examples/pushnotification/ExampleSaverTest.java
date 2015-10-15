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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.pushtechnology.diffusion.pushnotifications.persistence.Context;
import com.pushtechnology.diffusion.pushnotifications.persistence.Loader;

public class ExampleSaverTest {
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Mock
    private Context context;

    @Mock
    private ModelCodec modelCodec;

    private File noSuchFile;
    private File existingFile;
    private Model exampleModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        noSuchFile = new File(folder.getRoot(), "noSuchFile");
        existingFile = folder.newFile();

        exampleModel = new Model();
        final Set<String> subscribers = new HashSet<>();
        subscribers.add("foo://bar");
        exampleModel.getState().put("some/topic", subscribers);
    }

    @Test
    public void testExampleNoFileSaver() throws Exception {
        final ExampleSaver exampleSaver = new ExampleSaver(noSuchFile, modelCodec);
        assertSame(exampleSaver.getDbFile(), noSuchFile);
        assertThat(exampleSaver.getModel(), notNullValue());
    }

    @Test
    public void testExampleWithFileSaver() throws Exception {
        final ModelCodec codec = mock(ModelCodec.class);
        when(codec.load()).thenReturn(exampleModel);

        final ExampleSaver exampleSaver = new ExampleSaver(existingFile, codec);
        assertSame(exampleSaver.getDbFile(), existingFile);
        assertSame(exampleSaver.getModel(), exampleModel);
    }

    @Test
    public void testOnAddedSubscription() throws Exception {
        final ExampleSaver exampleSaver = new ExampleSaver(noSuchFile, modelCodec);
        exampleSaver.onAddedSubscription("some/topic", "foo://bar", context);

        final ArgumentCaptor<Model> captor = ArgumentCaptor.forClass(Model.class);
        verify(modelCodec).save(captor.capture());

        assertThat(captor.getValue().getState(), hasKey("some/topic"));
        assertThat(captor.getValue().getState().get("some/topic"), contains("foo://bar"));
    }

    @Test
    public void testOnRemovedSubscription() throws Exception {
        final ExampleSaver exampleSaver = new ExampleSaver(noSuchFile, exampleModel, modelCodec);
        exampleSaver.onRemovedSubscription("some/topic", "foo://bar", context);

        final ArgumentCaptor<Model> captor = ArgumentCaptor.forClass(Model.class);
        verify(modelCodec).save(captor.capture());

        assertThat(captor.getValue().getState(), not(hasKey("some/topic")));
    }

    @Test
    public void testOnReplacedSubscription() throws Exception {

        final ExampleSaver exampleSaver = new ExampleSaver(noSuchFile, exampleModel, modelCodec);
        exampleSaver.onReplacedSubscription("some/topic", "foo://bar", "foo://baz", context);

        final ArgumentCaptor<Model> captor = ArgumentCaptor.forClass(Model.class);
        verify(modelCodec).save(captor.capture());

        assertThat(captor.getValue().getState(), hasKey("some/topic"));
        assertThat(captor.getValue().getState().get("some/topic"), contains("foo://baz"));
    }

    @Test
    public void testSetLoader() throws Exception {
        final Loader loader = mock(Loader.class);
        final Model mockModel = mock(Model.class);
        final ExampleSaver exampleSaver = new ExampleSaver(noSuchFile, mockModel, modelCodec);
        exampleSaver.setLoader(loader);
        verify(mockModel).inject(loader);
    }

}

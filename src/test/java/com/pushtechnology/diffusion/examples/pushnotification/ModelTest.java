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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.pushtechnology.diffusion.examples.pushnotification.Model;
import com.pushtechnology.diffusion.pushnotifications.persistence.Loader;

public class ModelTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAddSubscription() throws Exception {
        final Model model = new Model();
        model.addSubscription("some/topic", "foo://bar");

        assertThat(model.getState(), hasKey("some/topic"));
        assertThat(model.getState().get("some/topic"), contains("foo://bar"));
    }

    @Test
    public void testAddTwoSubscriptions() throws Exception {
        final Model model = new Model();
        model.addSubscription("some/topic", "foo://bar");
        model.addSubscription("some/topic", "foo://baz");

        assertThat(model.getState(), hasKey("some/topic"));
        assertThat(model.getState().get("some/topic"), containsInAnyOrder("foo://bar", "foo://baz"));
    }

    @Test
    public void testRemoveSinglSubscription() throws Exception {
        final Model model = new Model();
        model.addSubscription("some/topic", "foo://bar");

        model.removeSubscription("some/topic", "foo://bar");
        assertThat(model.getState(), not(hasKey("some/topic")));
    }

    @Test
    public void testAddManyRemoveSingleSubscription() throws Exception {
        final Model model = new Model();
        model.addSubscription("some/topic", "foo://bar");
        model.addSubscription("some/topic", "foo://baz");

        model.removeSubscription("some/topic", "foo://bar");
        model.removeSubscription("some/other/topic", "foo://baz");

        assertThat(model.getState(), hasKey("some/topic"));
        assertThat(model.getState().get("some/topic"), contains("foo://baz"));
    }

    @Test
    public void testInject() throws Exception {
        final Model model = new Model();
        model.addSubscription("some/topic", "foo://bar");
        model.addSubscription("some/other/topic", "foo://baz");
        Loader loader = mock(Loader.class);

        model.inject(loader);
        verify(loader).addSubscription("some/topic", "foo://bar");
        verify(loader).addSubscription("some/other/topic", "foo://baz");
    }

}

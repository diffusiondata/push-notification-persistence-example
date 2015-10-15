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

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pushtechnology.diffusion.pushnotifications.persistence.DestinationParseException;
import com.pushtechnology.diffusion.pushnotifications.persistence.Loader;

/**
 * Encapsulation of the model of topics to destinations.
 *
 * @author Push Technology Limited
 * @since 1.0
 */
/*package*/ class Model implements Serializable {

    private static final long serialVersionUID = 6804961102273794965L;

    private Map<String, Set<String>> state = new HashMap<>();

    /*protected*/ Map<String, Set<String>> getState() {
        return state;
    }

    /**
     * Add a subscription to the model.
     *
     * @param topicPath subject topic path
     * @param destination destination that has subscribed to {@code topicPath}
     */
    public synchronized void addSubscription(String topicPath, String destination) {
        final Set<String> subscriptions;
        if (state.containsKey(topicPath)) {
            subscriptions = state.get(topicPath);
        }
        else {
            subscriptions = new HashSet<String>();
            state.put(topicPath, subscriptions);
        }

        subscriptions.add(destination);
    }

    /**
     * Remove a subscription to the model.
     *
     * @param topicPath subject topic path
     * @param destination destination that no longer subscribes to {@code topicPath}
     */
    public void removeSubscription(String topicPath, String destination) {
        final Set<String> subscriptions = state.get(topicPath);
        if (subscriptions != null) {
            subscriptions.remove(destination);
            if (subscriptions.isEmpty()) {
                state.remove(topicPath);
            }
        }
    }

    /**
     *
     * Inject the model into the Push Notification Bridge.
     *
     * @param loader supplied by the bridge, used to inject state into the Push Notification Bridge
     * @throws DestinationParseException if destination cannot be parsed
     */
    public void inject(Loader loader) throws DestinationParseException {
        for (Map.Entry<String, Set<String>> entry : state.entrySet()) {
            final String topicPath = entry.getKey();
            final Set<String> destinations = entry.getValue();
            for (String destination : destinations) {
                loader.addSubscription(topicPath, destination);
            }
        }
    }

}

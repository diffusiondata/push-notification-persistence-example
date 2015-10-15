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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pushtechnology.diffusion.pushnotifications.persistence.Context;
import com.pushtechnology.diffusion.pushnotifications.persistence.DestinationParseException;
import com.pushtechnology.diffusion.pushnotifications.persistence.Loader;
import com.pushtechnology.diffusion.pushnotifications.persistence.Saver;

/**
 * Example of a {@link Saver} that persists its model using Java serialization.
 *<P>
 * Unsuitable for production purposes.
 *
 * @author Push Technology Limited
 * @since 1.0
 */
public class ExampleSaver implements Saver {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleSaver.class);
    private final File dbFile;
    private final Model model;
    private final ModelCodec loaderSaver;

    /*package*/ ExampleSaver(File dbFile, Model model, ModelCodec codec) {
        this.dbFile = dbFile;
        this.loaderSaver = codec;
        this.model = model;
    }

    /**
     * Constructor.
     * @param dbFile file to load from and save to.
     * @param codec delegate for loading and saving the model.
     */
    public ExampleSaver(File dbFile, ModelCodec codec) {
        this (dbFile, dbFile.exists()? codec.load() : new Model(), codec);
    }

    /*package*/ File getDbFile() {
        return dbFile;
    }

    /*package*/ Model getModel() {
        return model;
    }

    @Override
    public void onAddedSubscription(String topicPath, String destination, Context context) {
        LOG.info("Adding subscription to {} from {}, context {}", topicPath, destination, context);
        model.addSubscription(topicPath, destination);
        loaderSaver.save(model);
    }

    @Override
    public void onRemovedSubscription(String topicPath, String destination, Context context) {
        LOG.info("Removing subscription to {} by {}, context {}", topicPath, destination, context);
        model.removeSubscription(topicPath, destination);
        loaderSaver.save(model);
    }

    @Override
    public void onReplacedSubscription(String topicPath, String oldDest, String newDest, Context context) {
        LOG.info("Replacing {} with {} for subscription to {}", oldDest, newDest, topicPath);
        model.removeSubscription(topicPath, oldDest);
        model.addSubscription(topicPath, newDest);
        loaderSaver.save(model);
    }

    @Override
    public void setLoader(Loader loader) {
        try {
            model.inject(loader);
        }
        catch (DestinationParseException ex) {
            LOG.error("Cannot parse data in model stored in {}", dbFile, ex);
        }
    }
}

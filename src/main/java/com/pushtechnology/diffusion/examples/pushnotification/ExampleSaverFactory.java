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

import com.pushtechnology.diffusion.pushnotifications.persistence.Saver;
import com.pushtechnology.diffusion.pushnotifications.persistence.SaverFactory;

/**
 * Example {@link SaverFactory} that uses the string supplied from configuration as a filename, and
 * builds an {@link ExampleSaver} with that.
 *
 * @author Push Technology Limited
 * @since 1.0
 */
public class ExampleSaverFactory implements SaverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleSaverFactory.class);

    @Override
    public Saver build(String config) {
        LOG.info("Building a {}, usuitable for production purposes.");
        final File dbFile = new File(config);
        return new ExampleSaver(dbFile, new ModelCodec(dbFile));
    }

}

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes and decodes a {@link Model} to an from a file system.
 *<P>
 *
 * @author Push Technology Limited
 * @since 1.0
 */
/*package*/ class ModelCodec {
    private static final Logger LOG = LoggerFactory.getLogger(ModelCodec.class);

    private final File file;

    /*package*/ ModelCodec(File file) {
        this.file = file;
    }

    public void save(Model model) {
        try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(model);
        }
        catch (IOException ex) {
            LOG.error("Cannot save {}", file, ex);
            throw new RuntimeException(ex);
        }
    }

    public Model load() {
        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Model)ois.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            LOG.error("Cannot load {}", file, ex);
            throw new RuntimeException(ex);
        }
    }
}

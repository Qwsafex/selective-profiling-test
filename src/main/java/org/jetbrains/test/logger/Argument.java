package org.jetbrains.test.logger;

import java.io.Serializable;

class Argument implements Serializable{
    final String type;
    final String value;

    Argument(String type, String value) {
        this.type = type;
        this.value = value;
    }
}

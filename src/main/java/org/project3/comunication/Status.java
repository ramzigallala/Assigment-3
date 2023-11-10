package org.project3.comunication;

import java.io.Serializable;

public record Status(BrushManager brushManager, PixelGrid grid) implements Serializable {
}

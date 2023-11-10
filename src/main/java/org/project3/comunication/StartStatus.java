package org.project3.comunication;

import java.io.Serializable;

public record StartStatus(BrushManager brushManager, PixelGrid grid, int id) implements Serializable {
}

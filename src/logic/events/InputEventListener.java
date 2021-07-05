package logic.events;

import java.awt.Point;

import logic.*;
import logic.bricks.Brick;

public interface InputEventListener {
    DownData onDownEvent(MoveEvent event);
    void saveHighScore();
    boolean onShadowDown(Point shadowPos);
    boolean getCanMove();
    ViewData onLeftEvent();
    ViewData onRightEvent();
    ViewData onRotateEvent();
    Brick saveCurrentBrick(Brick previousBrick);
}

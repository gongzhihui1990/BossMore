package org.gong.bmw.model.sea;

import android.graphics.Canvas;
import android.graphics.Paint;

import net.gtr.framework.util.Loger;

import org.gong.bmw.model.GameItemDrawView;
import org.gong.bmw.model.GameItemState;
import org.gong.bmw.model.GamePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caroline
 * @date 2018/7/13
 */

public class Cloud extends GameItemDrawView {

    /**
     * 边界屏幕宽度千分比
     */
    private static final float BOUNDARY = 0.01f;
    private final int mCanvasHigh;
    private final int mCanvasWith;
    float speed = 0.0025f;
    boolean remove = false;
    private List<Cycle> cycles;
    private GamePoint point;

    public Cloud(int canvasHigh, int canvasWith) {
        point = new GamePoint(1, (float) (0.1f * Math.random()));
        mCanvasWith = canvasHigh;
        mCanvasHigh = canvasWith;
        cycles = new ArrayList<>();
        cycles.add(new Cycle());
        cycles.add(new Cycle());
        cycles.add(new Cycle());
        cycles.add(new Cycle());
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for (Cycle cycle : cycles) {
            drawCycle(canvas, cycle, paint);
        }
    }

    private void drawCycle(Canvas canvas, Cycle cycle, Paint paint) {
        Loger.INSTANCE.e("drawCircle1" + cycle.toString());
        float x = point.getX() * mCanvasWith + cycle.cx;
        if (x > mCanvasWith) {
            x = mCanvasWith;
        }
        canvas.drawCircle(x, point.getY() * mCanvasHigh + cycle.cy, cycle.cr, paint);
        Loger.INSTANCE.e("drawCircle2" + point.getX() * mCanvasWith + cycle.cx);

    }

    @Override
    public boolean shouldRemove() {
        return remove;
    }

    @Override
    public GameItemState getGameItemState() {
        return null;
    }

    @Override
    public void setGameItemState(GameItemState state) {

    }

    @Override
    public GamePoint getPosition() {
        return point;
    }

    @Override
    public final void move() {
        super.move();
        if (getBoundaryX() != BaseBoot.Boundary.Left) {
            point.moveX(-speed);
        } else {
            remove = true;
        }
    }

    private BaseBoot.Boundary getBoundaryX() {
        if (point.getX() < BOUNDARY) {
            return BaseBoot.Boundary.Left;
        }
        if (point.getX() > 1 - BOUNDARY) {
            return BaseBoot.Boundary.Right;
        }

        return BaseBoot.Boundary.Mid;
    }

    class Cycle {
        float cx;
        float cy;
        float cr;

        Cycle() {
            cx = (float) (30 * Math.random());
            cy = (float) (30 * Math.random());
            cr = (float) (20 * Math.random());
        }

        @Override
        public String toString() {
            return "cx" + cx + "cy" + cy + "cr" + cr;
        }
    }

}

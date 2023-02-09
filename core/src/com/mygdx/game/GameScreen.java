package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TODO
 *
 * @author Liuzhenbin
 * @date 2023/2/9 10:37
 **/
public class GameScreen implements Screen {

    DropGame game;
    OrthographicCamera camera;
    Texture bucketImage;
    Texture dropImage;
    Rectangle bucket;
    List<Rectangle> drops;
    long lastDropTime;
    int dropsGathered;

    public GameScreen(DropGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        bucketImage = new Texture("bucket.png");
        dropImage = new Texture("drop.png");
        bucket = new Rectangle();
        //水平居中
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        drops = new ArrayList<>();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        drops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle drop : drops) {
            game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
            game.batch.draw(dropImage, drop.x, drop.y);
        }
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 400 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 400 * delta;
        }
        if(bucket.x < 0) bucket.x = 0;
        if(bucket.x > 800 - 64) bucket.x = 800 - 64;
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            Gdx.app.exit();
        }

        if(TimeUtils.nanoTime() -lastDropTime > 1000000000) spawnRaindrop();

        for (Iterator<Rectangle> iter = drops.iterator(); iter.hasNext();) {
            Rectangle drop = iter.next();
            drop.y -= 200 * delta;
            if(drop.y + 64 < 0) iter.remove();
            if(drop.overlaps(bucket)) {
                dropsGathered++;
                iter.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bucketImage.dispose();
        dropImage.dispose();
    }
}

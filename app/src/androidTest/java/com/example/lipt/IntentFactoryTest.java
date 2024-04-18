/**
 * Guillermo Zendejas, Luis Hernandez
 * April 15, 2024
 * IntentFactoryTest.java, this class describes the Junit testing for our intent factory methods
 */

package com.example.lipt;

import static org.junit.Assert.assertEquals;
import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Objects;


@RunWith(AndroidJUnit4.class)
public class IntentFactoryTest {
    private static final String CURRENT_USERNAME = "Active User";

    private static final String ADMIN_USER_ID_EXTRA_KEY = "com.example.lipt.ADMIN_ACTIVITY_USER_ID";
    private static final int CURRENT_USER_ID = 0;
    private static final String FINAL_SCORE = "hopefully ten";
    private final int currentId = 1;
    private final int finalScore = 10;
    private Context context;


    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
    }

    @After
    public void tearDown() throws Exception {
    }

    //main activity (login page) intent factory method test
    @Test
    public void mainFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = MainActivity.mainFactory(context);
        Class<?> activityClass = MainActivity.class;

        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //registration activity intent factory method test
    @Test
    public void registrationFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = registrationActivity.registrationFactory(context);
        Class<?> activityClass = registrationActivity.class;

        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //menu activity (landing page) intent factory method test
    @Test
    public void menuFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = MenuActivity.menuFactory(context, currentId);
        Class<?> activityClass = MenuActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //game activity intent factory method test
    @Test
    public void gameFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = GameActivity.gameFactory(context, currentId);
        Class<?> activityClass = GameActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //game result activity intent factory method test
    @Test
    public void gameResultFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = GameResultActivity.gameResultFactory(context, currentId, finalScore);
        Class<?> activityClass = GameResultActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);
        int expectedScore = 10;
        int actualScore = intent.getIntExtra(FINAL_SCORE, 0);

        assertEquals(expectedInt, actualInt);
        assertEquals(expectedScore, actualScore);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //pokedex activity intent factory method test
    @Test
    public void pokedexFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = PokedexActivity.pokedexFactory(context, currentId);
        Class<?> activityClass = PokedexActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //trainer record activity intent factory method test
    @Test
    public void trainerRecordFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = trainerRecordActivity.trainerRecordFactory(context, currentId);
        Class<?> activityClass = trainerRecordActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //prize activity intent factory method test
    @Test
    public void prizeFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = PrizeActivity.prizeFactory(context, currentId);
        Class<?> activityClass = PrizeActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }



    //admin activity intent factory method test
    @Test
    public void adminActivityIntentFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = AdminActivity.adminActivityIntentFactory(context, currentId);
        Class<?> activityClass = AdminActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(ADMIN_USER_ID_EXTRA_KEY, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }

    //credits activity intent factory method test
    @Test
    public void creditsFactory() {
        context = ApplicationProvider.getApplicationContext();
        Intent intent = CreditsActivity.creditsFactory(context, currentId);
        Class<?> activityClass = CreditsActivity.class;
        int expectedInt = 1;
        int actualInt = intent.getIntExtra(CURRENT_USERNAME, CURRENT_USER_ID);

        assertEquals(expectedInt, actualInt);
        assertEquals(activityClass.getName(), Objects.requireNonNull(intent.getComponent()).getClassName());
    }
}

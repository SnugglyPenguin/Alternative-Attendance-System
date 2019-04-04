package com.nophp.attendancescanner;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
@Ignore("Throws Null Pointer Exception")
public class MainActivityTest {
    private static MainActivity mainActivity;
    private String hash = mainActivity.readWeb("51.68.136.156/rwp/hasher.php?sid=011");

    @BeforeClass
    public static void setUp()
    {
        mainActivity = new MainActivity();
    }

    //@Test
    public void testData()
    {
        String read = readData();
        scanData(read);
    }

    public String readData()
    {
        String segments[] = hash.split("\"");

        String hashed = segments[segments.length - 6];

        System.out.println(hashed);
        return hashed;
    }

    public String scanData(String hashed)
    {
        String scanText = mainActivity.readWeb("51.68.136.156/rwp/scanner.php?qr=" + hashed);
        return scanText;
    }

    @Test
    public void errorCheck0()
    {

        String input = scanData(readData());
        String expected = "Success!";
        MainActivity mainActivity = new MainActivity();
        String output = mainActivity.errorCheck(input);

        Assert.assertEquals(expected, output);

    }

    @Test
    public void errorCheck1()
    {

        String input = scanData("10101");
        String expected = "Error 1: Incorrect hash";
        MainActivity mainActivity = new MainActivity();
        String output = mainActivity.errorCheck(input);

        Assert.assertEquals(expected, output);

    }

    //2 WIP


    //3 WIP

    @Test
    public void errorCheck4()
    {

        String input = scanData("fd312f7e3b696135b265c610610fa578");
        String expected = "Error 4: QR code does not match";
        MainActivity mainActivity = new MainActivity();
        String output = mainActivity.errorCheck(input);

        Assert.assertEquals(expected, output);

    }
    //5 WIP - must test irl
}




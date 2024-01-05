package rege.rege.utf8chr.test;

import java.util.Arrays;
import java.util.logging.Logger;

import rege.rege.utf8chr.UTF8Char;
import rege.rege.utf8chr.UTF8Sequence;

/**
 * @author REGE
 * @since 0.0.1a1
 */
public class Test1 {
    public static final Logger LOGGER =
    Logger.getLogger("rege.rege.utf8chr.test.Test1");

    /**
     * <p>Run the test code.</p>
     * <h4>Expected output:</h4>
     * <ol>
     * <li>Hello world</li>
     * <li>world</li>
     * <li>Hello</li>
     * <li>&#20320;&#22909;</li>
     * <li>Hello world!Hello world</li>
     * <li>H</li>
     * <li>true</li>
     * <li>false</li>
     * <li>HELLO WORLD</li>
     * <li>H!e!l!l!o! !w!o!r!l!d</li>
     * <li>&#20320;\udce5\udca5!</li>
     * <li>Hello world!!!!</li>
     * <li>!!!!Hello world</li>
     * <li>    Hello world</li>
     * <li>Hello worldHello worldHello worldHello worldHello world</li>
     * <li>Hello world</li>
     * <li></li>
     * <li>2</li>
     * <li>9</li>
     * <li>[2, 3, 9]</li>
     * <li>[He, , o wor, d]</li>
     * <li>o wor</li>
     * <li>(He, l, lo world)</li>
     * <li>He11o wor1d</li>
     * <li>(*P&#381;&#9;&#20481;</li>
     * <li>dlrow olleH</li>
     * <li>dlrow olle</li>
     * <li>Hlowrd</li>
     * </ol>
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        final UTF8Sequence HELLO = new UTF8Sequence("Hello world");
        LOGGER.info(HELLO.toString());
        LOGGER.info(HELLO.subSequence(6).toString());
        LOGGER.info(HELLO.subSequence(0, 5).toString());
        LOGGER.info(UTF8Sequence
                    .decodeFrom(new byte[]{-28, -67, -96, -27, -91, -67})
                    .toString());
        LOGGER.info(new UTF8Sequence('!')
                    .join(new UTF8Sequence[]{HELLO, HELLO}).toString());
        LOGGER.info(HELLO.charAt(0).toString());
        LOGGER.info(Boolean.toString(HELLO.subSequence(6)
                                     .equals(new UTF8Sequence("world"))));
        LOGGER.info(Boolean.toString(HELLO.subSequence(6)
                                     .equals(new UTF8Sequence("World"))));
        LOGGER.info(HELLO.upper().toString());
        LOGGER.info(UTF8Sequence.join(new UTF8Char('!'), HELLO.singles())
                    .toString());
        LOGGER.info(UTF8Sequence
                    .decodeFrom(new byte[]{-28, -67, -96, -27, -91, 33},
                                "surrogateescape").toString());
        LOGGER.info(HELLO.ljust(15, new UTF8Char('!')).toString());
        LOGGER.info(HELLO.rjust(15, new UTF8Char('!')).toString());
        LOGGER.info(HELLO.rjust(15).toString());
        LOGGER.info(HELLO.repeat(5).toString());
        LOGGER.info(HELLO.repeat(1).toString());
        LOGGER.info(HELLO.times(-1).toString());
        LOGGER.info(Integer.toString(HELLO.indexOf(new UTF8Char('l'))));
        LOGGER.info(Integer.toString(HELLO.lastIndexOf(new UTF8Char('l'))));
        LOGGER.info(Arrays.toString(HELLO.indicesOf(new UTF8Char('l'))));
        LOGGER.info(Arrays.toString(HELLO.split(new UTF8Char('l'))));
        LOGGER.info(HELLO.strip(new UTF8Sequence("Held")).toString());
        LOGGER.info(HELLO.partition(new UTF8Char('l')).toString());
        LOGGER.info(HELLO.replace(new UTF8Char('l'), new UTF8Char('1'))
                    .toString());
        LOGGER.info(new UTF8Sequence("\\x50\\x4a\\120\\575\\n\\u5001")
                    .unescape().toString());
        LOGGER.info(HELLO.slice(null, null, Integer.valueOf(-1)).toString());
        LOGGER.info(HELLO.slice(null, Integer.valueOf(0),
                                Integer.valueOf(-1)).toString());
        LOGGER.info(HELLO.slice(null, null, Integer.valueOf(2)).toString());
    }
}

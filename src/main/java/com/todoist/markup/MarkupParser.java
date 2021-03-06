package com.todoist.markup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MarkupParser {
    public static final int HEADER = 1;
    public static final int CODE_BLOCK = 2;
    public static final int CODE_INLINE = 4;
    public static final int GMAIL = 8;
    public static final int OUTLOOK = 16;
    public static final int THUNDERBIRD = 32;
    public static final int MARKDOWN_LINK = 64;
    public static final int LINK = 128;
    public static final int BOLD = 256;
    public static final int ITALIC = 512;
    public static final int EMOJI = 1024;

    public static final int ALL = 2047;

    /**
     * Returns all {@link MarkupEntry} that matches this {@code string}.
     */
    public static List<MarkupEntry> getMarkupEntries(CharSequence input) {
        return getMarkupEntries(input, ALL);
    }

    /**
     * Returns all {@link MarkupEntry} of the type(s) set in {@code flags} that matches this {@code string}.
     */
    public static List<MarkupEntry> getMarkupEntries(CharSequence input, int flags) {
        List<MarkupEntry> markupEntries = new ArrayList<>();

        if (input != null) {
            if ((flags & HEADER) == HEADER) {
                parseHeaderMarkupEntries(input, markupEntries);
            }

            if ((flags & CODE_BLOCK) == CODE_BLOCK) {
                parseCodeBlockMarkupEntries(input, markupEntries);
            }

            if ((flags & CODE_INLINE) == CODE_INLINE) {
                parseCodeInlineMarkupEntries(input, markupEntries);
            }

            if ((flags & GMAIL) == GMAIL) {
                parseGmailMarkupEntries(input, markupEntries);
            }

            if ((flags & OUTLOOK) == OUTLOOK) {
                parseOutlookMarkupEntries(input, markupEntries);
            }

            if ((flags & THUNDERBIRD) == THUNDERBIRD) {
                parseThunderbirdMarkupEntries(input, markupEntries);
            }

            if ((flags & MARKDOWN_LINK) == MARKDOWN_LINK) {
                parseMarkdownLinkMarkupEntries(input, markupEntries);
            }

            if ((flags & LINK) == LINK) {
                parseLinkMarkupEntries(input, markupEntries);
            }

            if ((flags & BOLD) == BOLD) {
                parseBoldMarkupEntries(input, markupEntries);
            }

            if ((flags & ITALIC) == ITALIC) {
                parseItalicMarkupEntries(input, markupEntries);
            }

            if ((flags & EMOJI) == EMOJI) {
                parseEmojiMarkupEntries(input, markupEntries);
            }
        }

        return markupEntries;
    }

    private static void parseHeaderMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.HEADER.matcher(input);

        if (matcher.find()) {
            String text = matcher.group(1);
            if (text == null) {
                text = matcher.group(2);
            }
            markupEntries.add(new MarkupEntry(MarkupType.HEADER, matcher.start(), matcher.end(), text));
        }
    }

    private static void parseCodeBlockMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.CODE_BLOCK.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(1);
            markupEntries.add(new MarkupEntry(MarkupType.CODE_BLOCK, matcher.start(), matcher.end(), text));
        }
    }

    private static void parseCodeInlineMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.CODE_INLINE.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(1);
            markupEntries.add(new MarkupEntry(MarkupType.INLINE_CODE, matcher.start(), matcher.end(), text));
        }
    }

    private static void parseGmailMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.GMAIL.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(2);
            String link = matcher.group(1);
            markupEntries.add(new MarkupEntry(MarkupType.GMAIL, matcher.start(), matcher.end(), text, link));
        }
    }

    private static void parseOutlookMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.OUTLOOK.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(2);
            String link = matcher.group(1);
            markupEntries.add(new MarkupEntry(MarkupType.OUTLOOK, matcher.start(), matcher.end(), text, link));
        }
    }

    private static void parseThunderbirdMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.THUNDERBIRD.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(1);
            String link = matcher.group(2);
            markupEntries.add(new MarkupEntry(MarkupType.THUNDERBIRD, matcher.start(), matcher.end(), text, link));
        }
    }

    private static void parseMarkdownLinkMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.MARKDOWN_LINK.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(1);
            String link = matcher.group(2);
            markupEntries.add(new MarkupEntry(MarkupType.LINK, matcher.start(), matcher.end(), text, link));
        }
    }

    private static void parseLinkMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.LINK.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(2);
            String link = matcher.group(1);

            if (text == null) {
                link = link != null ? link : matcher.group(0);
                markupEntries.add(new MarkupEntry(MarkupType.LINK, matcher.start(), matcher.end(), link, link));
            } else {
                markupEntries.add(new MarkupEntry(MarkupType.LINK, matcher.start(), matcher.end(), text, link));
            }
        }
    }

    private static void parseBoldMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.BOLD.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(2);
            markupEntries.add(new MarkupEntry(MarkupType.BOLD, matcher.start(1), matcher.end(1), text));
        }
    }

    private static void parseItalicMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = Patterns.ITALIC.matcher(input);

        while (matcher.find()) {
            String text = matcher.group(2);
            markupEntries.add(new MarkupEntry(MarkupType.ITALIC, matcher.start(1), matcher.end(1), text));
        }
    }

    private static void parseEmojiMarkupEntries(CharSequence input, List<MarkupEntry> markupEntries) {
        Matcher matcher = EmojiParser.getEmojiPattern().matcher(input);

        while (matcher.find()) {
            String emoji = getEmoji(matcher.group());
            if (emoji != null) {
                markupEntries.add(new MarkupEntry(MarkupType.EMOJI, matcher.start(), matcher.end(), emoji));
            }
        }
    }

    public static String getEmoji(String key) {
        return EmojiParser.getEmoji(key);
    }

    public static void init() {
        EmojiParser.init();
    }
}

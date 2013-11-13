

package com.example.hangman;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines a contract between the Hangman content provider and its clients. A contract defines the
 * information that a client needs to access the provider as one or more data tables. A contract
 * is a public, non-extendable (final) class that contains constants defining column names and
 * URIs. A well-written client depends only on the constants in the contract.
 */
public final class HangmanContent {
    public static final String AUTHORITY = "com.example.provider.hangmancontent";

    // This class cannot be instantiated
    private HangmanContent() {
    }

    /**
     * Notes table contract
     */
    public static final class Words implements BaseColumns {

        // This class cannot be instantiated
        private Words() {}

        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = "words";

        /*
         * URI definitions
         */

        /**
         * The scheme part for this provider's URI
         */
        private static final String SCHEME = "content://";

        /**
         * Path parts for the URIs
         */

        /**
         * Path part for the Notes URI
         */
        private static final String PATH_WORDS = "/words";

        /**
         * Path part for the Note ID URI
         */
        private static final String PATH_WORD_ID = "/words/";

        /**
         * 0-relative position of a note ID segment in the path part of a note ID URI
         */
        public static final int WORD_ID_PATH_POSITION = 1;

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_WORDS);

        /**
         * The content URI base for a single note. Callers must
         * append a numeric note id to this Uri to retrieve a note
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_WORD_ID);

        /**
         * The content URI match pattern for a single note, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
            = Uri.parse(SCHEME + AUTHORITY + PATH_WORD_ID + "/#");

        /*
         * MIME type definitions
         */
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.word";
        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.word";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "word DESC";

        /*
         * Column definitions
         */

        /**
         * Column name for the title of the note
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_TITLE = "title";

        /**
         * Column name of the note content
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_WORD = "word";

    }
}

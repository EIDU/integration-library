package com.eidu.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class TestUtil {
    @NonNull
    public static Context contextWithMockResolver(
            @NonNull String givenAuthority, @NonNull Uri givenUri, @NonNull String givenContent) {
        ContentProvider provider = mockContentProvider(givenUri, givenContent);
        return new MockContext() {
            @Override
            public ContentResolver getContentResolver() {
                MockContentResolver resolver = new MockContentResolver();
                resolver.addProvider(givenAuthority, provider);
                return resolver;
            }
        };
    }

    private static ContentProvider mockContentProvider(Uri givenUri, String givenContent) {
        return new MockContentProvider() {
            @Nullable
            @Override
            public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) {
                assertEquals("r", mode);
                assertEquals(givenUri, uri);
                try {
                    File file = tempFileWithContent(givenContent);
                    return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                } catch (IOException e) {
                    throw new RuntimeException("Test failure", e);
                }
            }

            @Override
            public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeType, Bundle opts) {
                return new AssetFileDescriptor(openFile(uri, "r"), 0, -1);
            }
        };
    }

    @NonNull
    public static File tempFileWithContent(@NonNull String givenContent) throws IOException {
        File file = File.createTempFile("file", ".test");
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file))) {
            out.write(givenContent);
        }
        return file;
    }

    @NonNull
    public static String readLine(@NonNull FileDescriptor file) throws IOException {
        return readLine(new FileInputStream(file));
    }

    @NonNull
    public static String readLine(@NonNull InputStream stream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.readLine();
        }
    }
}

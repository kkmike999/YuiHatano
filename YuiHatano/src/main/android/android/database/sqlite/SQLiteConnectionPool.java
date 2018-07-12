/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.database.sqlite;

import android.os.CancellationSignal;

import java.io.Closeable;
import java.io.IOException;

public final class SQLiteConnectionPool implements Closeable {

    /**
     * Connection flag: Read-only.
     * <p>
     * This flag indicates that the connection will only be used to
     * perform read-only operations.
     * </p>
     */
    public static final int CONNECTION_FLAG_READ_ONLY = 1 << 0;

    /**
     * Connection flag: Primary connection affinity.
     * <p>
     * This flag indicates that the primary connection is required.
     * This flag helps support legacy applications that expect most data modifying
     * operations to be serialized by locking the primary database connection.
     * Setting this flag essentially implements the old "db lock" concept by preventing
     * an operation from being performed until it can obtain exclusive access to
     * the primary connection.
     * </p>
     */
    public static final int CONNECTION_FLAG_PRIMARY_CONNECTION_AFFINITY = 1 << 1;

    /**
     * Connection flag: Connection is being used interactively.
     * <p>
     * This flag indicates that the connection is needed by the UI thread.
     * The connection pool can use this flag to elevate the priority
     * of the database connection request.
     * </p>
     */
    public static final int CONNECTION_FLAG_INTERACTIVE = 1 << 2;

    @Override
    public void close() throws IOException {}

    public SQLiteConnection acquireConnection(String sql, int connectionFlags, CancellationSignal cancellationSignal) {
        return null;
    }

    public void releaseConnection(SQLiteConnection mConnection) {

    }

    public boolean shouldYieldConnection(SQLiteConnection mConnection, int mConnectionFlags) {
        return false;
    }
}

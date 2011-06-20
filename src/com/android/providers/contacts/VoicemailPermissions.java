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
 * limitations under the License
 */

package com.android.providers.contacts;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Provides method related to check various voicemail permissions under the
 * specified context.
 * <p> This is an immutable object.
 */
public class VoicemailPermissions {
    private final Context mContext;

    public VoicemailPermissions(Context context) {
        mContext = context;
    }

    /** Determines if the calling process has access to its own voicemails. */
    public boolean callerHasOwnVoicemailAccess() {
        return callerHasPermission(Manifest.permission.READ_WRITE_OWN_VOICEMAIL);
    }

    /** Determines if the calling process has access to all voicemails. */
    public boolean callerHasFullAccess() {
        return callerHasPermission(Manifest.permission.READ_WRITE_OWN_VOICEMAIL) &&
                callerHasPermission(Manifest.permission.READ_WRITE_ALL_VOICEMAIL);
    }

    /**
     * Checks that the caller has permissions to access its own voicemails.
     *
     * @throws SecurityException if the caller does not have the voicemail source permission.
     */
    public void checkCallerHasOwnVoicemailAccess() {
        if (!callerHasOwnVoicemailAccess()) {
            throw new SecurityException("The caller must have permission: " +
                    Manifest.permission.READ_WRITE_OWN_VOICEMAIL);
        }
    }

    /**
     * Checks that the caller has permissions to access ALL voicemails.
     *
     * @throws SecurityException if the caller does not have the voicemail source permission.
     */
    public void checkCallerHasFullAccess() {
        if (!callerHasFullAccess()) {
            throw new SecurityException(String.format("The caller must have permissions %s AND %s",
                    Manifest.permission.READ_WRITE_OWN_VOICEMAIL,
                    Manifest.permission.READ_WRITE_ALL_VOICEMAIL));
        }
    }

    /** Determines if the given package has access to its own voicemails. */
    public boolean packageHasOwnVoicemailAccess(String packageName) {
        return packageHasPermission(packageName, Manifest.permission.READ_WRITE_OWN_VOICEMAIL);
    }

    /** Determines if the given package has full access. */
    public boolean packageHasFullAccess(String packageName) {
        return packageHasPermission(packageName, Manifest.permission.READ_WRITE_OWN_VOICEMAIL) &&
                packageHasPermission(packageName, Manifest.permission.READ_WRITE_ALL_VOICEMAIL);
    }

    /** Determines if the given package has the given permission. */
    private boolean packageHasPermission(String packageName, String permission) {
        return mContext.getPackageManager().checkPermission(permission, packageName)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** Determines if the calling process has the given permission. */
    private boolean callerHasPermission(String permission) {
        return mContext.checkCallingPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}

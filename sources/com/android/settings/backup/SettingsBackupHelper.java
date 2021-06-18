package com.android.settings.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.os.ParcelFileDescriptor;
import com.android.settings.shortcut.CreateShortcutPreferenceController;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsBackupHelper extends BackupAgentHelper {
    public void onCreate() {
        super.onCreate();
        addHelper("no-op", new NoOpHelper());
    }

    public void onRestoreFinished() {
        super.onRestoreFinished();
        CreateShortcutPreferenceController.updateRestoredShortcuts(this);
    }

    private static class NoOpHelper implements BackupHelper {
        private final int VERSION_CODE;

        public void restoreEntity(BackupDataInputStream backupDataInputStream) {
        }

        public void writeNewStateDescription(ParcelFileDescriptor parcelFileDescriptor) {
        }

        private NoOpHelper() {
            this.VERSION_CODE = 1;
        }

        public void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(parcelFileDescriptor2.getFileDescriptor());
                if (getVersionCode(parcelFileDescriptor) != 1) {
                    backupDataOutput.writeEntityHeader("placeholder", 1);
                    backupDataOutput.writeEntityData(new byte[1], 1);
                }
                fileOutputStream.write(1);
                fileOutputStream.flush();
                fileOutputStream.close();
                return;
            } catch (IOException unused) {
                return;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }

        private int getVersionCode(ParcelFileDescriptor parcelFileDescriptor) {
            FileInputStream fileInputStream;
            if (parcelFileDescriptor == null) {
                return 0;
            }
            try {
                fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                int read = fileInputStream.read();
                fileInputStream.close();
                return read;
            } catch (IOException unused) {
                return 0;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        }
    }
}

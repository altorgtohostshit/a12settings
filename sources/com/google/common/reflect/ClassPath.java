package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;

public final class ClassPath {
    /* access modifiers changed from: private */
    public static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.m22on(" ").omitEmptyStrings();
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>() {
    };
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger(ClassPath.class.getName());

    public static class ResourceInfo {
        private final File file;
        final ClassLoader loader;
        private final String resourceName;

        /* renamed from: of */
        static ResourceInfo m29of(File file2, String str, ClassLoader classLoader) {
            if (str.endsWith(".class")) {
                return new ClassInfo(file2, str, classLoader);
            }
            return new ResourceInfo(file2, str, classLoader);
        }

        ResourceInfo(File file2, String str, ClassLoader classLoader) {
            this.file = (File) Preconditions.checkNotNull(file2);
            this.resourceName = (String) Preconditions.checkNotNull(str);
            this.loader = (ClassLoader) Preconditions.checkNotNull(classLoader);
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ResourceInfo)) {
                return false;
            }
            ResourceInfo resourceInfo = (ResourceInfo) obj;
            if (!this.resourceName.equals(resourceInfo.resourceName) || this.loader != resourceInfo.loader) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(File file, String str, ClassLoader classLoader) {
            super(file, str, classLoader);
            this.className = ClassPath.getClassName(str);
        }

        public String toString() {
            return this.className;
        }
    }

    static abstract class Scanner {
        private final Set<File> scannedUris = Sets.newConcurrentHashSet();

        /* access modifiers changed from: protected */
        public abstract void scanResource(ResourceInfo resourceInfo) throws IOException;

        Scanner() {
        }

        /* access modifiers changed from: package-private */
        public final void scan(File file, ClassLoader classLoader) throws IOException {
            if (this.scannedUris.add(file.getCanonicalFile())) {
                scanFrom(file, classLoader);
            }
        }

        /* access modifiers changed from: protected */
        public void scanFrom(File file, ClassLoader classLoader) throws IOException {
            try {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        scanDirectory(classLoader, file);
                    } else {
                        scanJar(file, classLoader);
                    }
                }
            } catch (SecurityException e) {
                Logger access$100 = ClassPath.logger;
                access$100.warning("Cannot access " + file + ": " + e);
            }
        }

        private void scanJar(File file, ClassLoader classLoader) throws IOException {
            try {
                JarFile jarFile = new JarFile(file);
                try {
                    UnmodifiableIterator<File> it = getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                    while (it.hasNext()) {
                        scan(it.next(), classLoader);
                    }
                    scanJarFile(classLoader, jarFile);
                } finally {
                    try {
                        jarFile.close();
                    } catch (IOException unused) {
                    }
                }
            } catch (IOException unused2) {
            }
        }

        static ImmutableSet<File> getClassPathFromManifest(File file, Manifest manifest) {
            if (manifest == null) {
                return ImmutableSet.m25of();
            }
            ImmutableSet.Builder builder = ImmutableSet.builder();
            String value = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
            if (value != null) {
                for (String next : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(value)) {
                    try {
                        URL classPathEntry = getClassPathEntry(file, next);
                        if (classPathEntry.getProtocol().equals("file")) {
                            builder.add((Object) ClassPath.toFile(classPathEntry));
                        }
                    } catch (MalformedURLException unused) {
                        Logger access$100 = ClassPath.logger;
                        access$100.warning("Invalid Class-Path entry: " + next);
                    }
                }
            }
            return builder.build();
        }

        static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classLoader) {
            LinkedHashMap newLinkedHashMap = Maps.newLinkedHashMap();
            ClassLoader parent = classLoader.getParent();
            if (parent != null) {
                newLinkedHashMap.putAll(getClassPathEntries(parent));
            }
            UnmodifiableIterator<URL> it = getClassLoaderUrls(classLoader).iterator();
            while (it.hasNext()) {
                URL next = it.next();
                if (next.getProtocol().equals("file")) {
                    File file = ClassPath.toFile(next);
                    if (!newLinkedHashMap.containsKey(file)) {
                        newLinkedHashMap.put(file, classLoader);
                    }
                }
            }
            return ImmutableMap.copyOf(newLinkedHashMap);
        }

        private static ImmutableList<URL> getClassLoaderUrls(ClassLoader classLoader) {
            if (classLoader instanceof URLClassLoader) {
                return ImmutableList.copyOf((E[]) ((URLClassLoader) classLoader).getURLs());
            }
            if (classLoader.equals(ClassLoader.getSystemClassLoader())) {
                return parseJavaClassPath();
            }
            return ImmutableList.m23of();
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(8:3|4|5|7|8|15|12|1) */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0039, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0050, code lost:
            r4 = com.google.common.reflect.ClassPath.access$100();
            r5 = java.util.logging.Level.WARNING;
            r4.log(r5, "malformed classpath entry: " + r2, r3);
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x003b */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static com.google.common.collect.ImmutableList<java.net.URL> parseJavaClassPath() {
            /*
                com.google.common.collect.ImmutableList$Builder r0 = com.google.common.collect.ImmutableList.builder()
                com.google.common.base.StandardSystemProperty r1 = com.google.common.base.StandardSystemProperty.PATH_SEPARATOR
                java.lang.String r1 = r1.value()
                com.google.common.base.Splitter r1 = com.google.common.base.Splitter.m22on((java.lang.String) r1)
                com.google.common.base.StandardSystemProperty r2 = com.google.common.base.StandardSystemProperty.JAVA_CLASS_PATH
                java.lang.String r2 = r2.value()
                java.lang.Iterable r1 = r1.split(r2)
                java.util.Iterator r1 = r1.iterator()
            L_0x001c:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x006b
                java.lang.Object r2 = r1.next()
                java.lang.String r2 = (java.lang.String) r2
                java.io.File r3 = new java.io.File     // Catch:{ SecurityException -> 0x003b }
                r3.<init>(r2)     // Catch:{ SecurityException -> 0x003b }
                java.net.URI r3 = r3.toURI()     // Catch:{ SecurityException -> 0x003b }
                java.net.URL r3 = r3.toURL()     // Catch:{ SecurityException -> 0x003b }
                r0.add((java.lang.Object) r3)     // Catch:{ SecurityException -> 0x003b }
                goto L_0x001c
            L_0x0039:
                r3 = move-exception
                goto L_0x0050
            L_0x003b:
                java.net.URL r3 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0039 }
                java.lang.String r4 = "file"
                r5 = 0
                java.io.File r6 = new java.io.File     // Catch:{ MalformedURLException -> 0x0039 }
                r6.<init>(r2)     // Catch:{ MalformedURLException -> 0x0039 }
                java.lang.String r6 = r6.getAbsolutePath()     // Catch:{ MalformedURLException -> 0x0039 }
                r3.<init>(r4, r5, r6)     // Catch:{ MalformedURLException -> 0x0039 }
                r0.add((java.lang.Object) r3)     // Catch:{ MalformedURLException -> 0x0039 }
                goto L_0x001c
            L_0x0050:
                java.util.logging.Logger r4 = com.google.common.reflect.ClassPath.logger
                java.util.logging.Level r5 = java.util.logging.Level.WARNING
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r7 = "malformed classpath entry: "
                r6.append(r7)
                r6.append(r2)
                java.lang.String r2 = r6.toString()
                r4.log(r5, r2, r3)
                goto L_0x001c
            L_0x006b:
                com.google.common.collect.ImmutableList r0 = r0.build()
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.reflect.ClassPath.Scanner.parseJavaClassPath():com.google.common.collect.ImmutableList");
        }

        static URL getClassPathEntry(File file, String str) throws MalformedURLException {
            return new URL(file.toURI().toURL(), str);
        }

        /* access modifiers changed from: package-private */
        public void scanJarFile(ClassLoader classLoader, JarFile jarFile) throws IOException {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry nextElement = entries.nextElement();
                if (!nextElement.isDirectory() && !nextElement.getName().equals("META-INF/MANIFEST.MF")) {
                    scanResource(ResourceInfo.m29of(new File(jarFile.getName()), nextElement.getName(), classLoader));
                }
            }
        }

        private void scanDirectory(ClassLoader classLoader, File file) throws IOException {
            HashSet hashSet = new HashSet();
            hashSet.add(file.getCanonicalFile());
            scanDirectory(file, classLoader, "", hashSet);
        }

        private void scanDirectory(File file, ClassLoader classLoader, String str, Set<File> set) throws IOException {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                ClassPath.logger.warning("Cannot read directory " + file);
                return;
            }
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (file2.isDirectory()) {
                    File canonicalFile = file2.getCanonicalFile();
                    if (set.add(canonicalFile)) {
                        scanDirectory(canonicalFile, classLoader, str + name + "/", set);
                        set.remove(canonicalFile);
                    }
                } else {
                    String str2 = str + name;
                    if (!str2.equals("META-INF/MANIFEST.MF")) {
                        scanResource(ResourceInfo.m29of(file2, str2, classLoader));
                    }
                }
            }
        }
    }

    static final class DefaultScanner extends Scanner {
        private final SetMultimap<ClassLoader, ResourceInfo> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();

        DefaultScanner() {
        }

        /* access modifiers changed from: protected */
        public void scanResource(ResourceInfo resourceInfo) {
            this.resources.put(resourceInfo.loader, resourceInfo);
        }
    }

    static String getClassName(String str) {
        return str.substring(0, str.length() - 6).replace('/', '.');
    }

    static File toFile(URL url) {
        Preconditions.checkArgument(url.getProtocol().equals("file"));
        try {
            return new File(url.toURI());
        } catch (URISyntaxException unused) {
            return new File(url.getPath());
        }
    }
}

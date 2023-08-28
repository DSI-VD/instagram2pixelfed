package ch.vd.tools.ig2fedi.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public final class FileBackedPersistentList<T extends Serializable> extends AbstractList<T> {

    private final Path file;
    private final List<T> delegate;

    public FileBackedPersistentList(Path file) {
        this.file = file;
        this.delegate = load();
    }

    @SuppressWarnings("unchecked")
    private LinkedList<T> load() {
        if (!Files.exists(file)) {
            return new LinkedList<>();
        }

        try (var in = Files.newInputStream(file, READ)) {
            var input = new ObjectInputStream(in);
            return (LinkedList<T>) input.readObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private void save() {
        try (var out = Files.newOutputStream(file, WRITE, CREATE, TRUNCATE_EXISTING)) {
            var output = new ObjectOutputStream(out);
            output.writeObject(delegate);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public T get(int index) {
        return delegate.get(index);
    }

    @Override
    public boolean add(T t) {
        delegate.add(t);
        save();
        return true;
    }

    @Override
    public T remove(int index) {
        delegate.remove(index);
        save();
        return null;
    }

    @Override
    public int size() {
        return delegate.size();
    }
}

package uz.khakimjanovich.homework.cli;

final class RawTerminal implements AutoCloseable {

    private RawTerminal() {
    }

    static RawTerminal enable() throws Exception {
        if (System.console() == null) {
            return new RawTerminal();
        }
        new ProcessBuilder("sh", "-c", "stty -icanon min 1 -echo < /dev/tty")
                .inheritIO()
                .start()
                .waitFor();
        return new RawTerminal();
    }

    @Override
    public void close() throws Exception {
        if (System.console() == null) {
            return;
        }
        new ProcessBuilder("sh", "-c", "stty sane < /dev/tty")
                .inheritIO()
                .start()
                .waitFor();
    }
}


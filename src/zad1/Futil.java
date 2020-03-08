package zad1;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resFileName) {
        try {
            Charset encode = StandardCharsets.UTF_8;
            Charset decode = Charset.forName("Cp1250");
            FileChannel resChannel = FileChannel.open(Paths.get(resFileName), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            FileVisitor<Path> myFileVisitor = new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileChannel readChannel = FileChannel.open(file, StandardOpenOption.READ);
                    ByteBuffer byteBuffer = ByteBuffer.allocate((int)readChannel.size());
                    readChannel.read(byteBuffer);
                    byteBuffer.flip();
                    CharBuffer charBuffer = decode.decode(byteBuffer);
                    byteBuffer = encode.encode(charBuffer);
                    resChannel.write(byteBuffer);
                    readChannel.close();
                    return FileVisitResult.CONTINUE;
                }
            };
            Path path = Paths.get(dirName);
            Files.walkFileTree(path, myFileVisitor);
            resChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

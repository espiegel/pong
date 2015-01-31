import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.IntStream;

/**
 * Created by Eidan on 1/31/2015.
 */
public class AssetManager {
    private static AssetManager assetManager;
    private Map<String, Queue<javazoom.jl.player.Player>> soundClips;
    private HashMap<String, String> soundTypes;

    private AssetManager() {
        init();
    }

    public static synchronized AssetManager getInstance() {
        if(assetManager == null) {
            assetManager = new AssetManager();
        }
        return assetManager;
    }

    private void init() {
        soundClips = new HashMap<>();

        soundTypes = new HashMap<>();
        soundTypes.put("ding", "mp3");

        IntStream.range(0, 3).forEach(r -> loadClip("ding"));
    }

    private void loadClip(String name) {
        try {
            InputStream is = Start.class.getResourceAsStream("/sounds/" + name + "." + soundTypes.get(name));
            javazoom.jl.player.Player playMP3 = new javazoom.jl.player.Player(is);

            Queue<javazoom.jl.player.Player> queue;
            if(soundClips.containsKey(name)) {
                queue = soundClips.get(name);
                queue.add(playMP3);
            } else {
                queue = new LinkedList<>();
                queue.add(playMP3);
                soundClips.put(name, queue);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void finishedClip(String name) {
        new Thread(() -> loadClip(name)).start();
    }

    public javazoom.jl.player.Player getClip(String name) {
        if(soundClips.containsKey(name)) {
            if(soundClips.get(name).isEmpty()) {
                loadClip(name);
                return null;
            } else {
                return soundClips.get(name).poll();
            }
        } else {
            return null;
        }
    }
}

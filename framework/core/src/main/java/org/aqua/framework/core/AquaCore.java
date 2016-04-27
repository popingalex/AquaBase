package org.aqua.framework.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aqua.Caster;
import org.aqua.framework.scene.AbstractScene;
import org.aqua.framework.ui.AbstractScreen;

public class AquaCore {
    private Map<String, AbstractScene>  sceneMap  = new HashMap<String, AbstractScene>();
    private Map<String, AbstractScreen> screenMap = new HashMap<String, AbstractScreen>();
    public AquaCore(Map<String, Object> config) {
        List<Map<String, Object>> screenDataList = Caster.cast(config.get("screens"));
        for (Map<String, Object> screenData : screenDataList) {
            registerScreen(screenData);
        }
        List<Map<String, Object>> sceneDataList = Caster.cast(config.get("scenes"));
        for (Map<String, Object> sceneData : sceneDataList) {
            registerScene(sceneData);
        }
        Map<String, Object> baseConfig = Caster.cast(config.get("base"));
        Map<String, Object> launcher = Caster.cast(baseConfig.get("launcher"));
        System.out.println("launch : " + launcher.get("id"));
    }

    private void registerScreen(Map<String, Object> screenData) {
        System.out.println("add screen : " + screenData.get("id"));
    }

    private void registerScene(Map<String, Object> sceneData) {
        System.out.println("add scene : " + sceneData.get("title"));
    }
}

package io.github.matirosen.bugreport.modules;

import io.github.matirosen.bugreport.nms.common.NMS;
import me.yushust.inject.Binder;
import me.yushust.inject.Module;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class PacketsModule implements Module {


    @Override
    public void configure(Binder binder) {
        binder.bind(NMS.class).toProvider(() -> {
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            String stringVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
            int version = Integer.parseInt(stringVersion.split("_")[1].split("_")[0]);

            Class<?> clazz = null;

            try {
                clazz = version >= 14 ? Class.forName("io.github.matirosen.bugreport.nms.defaults.NMSImpl")
                        : Class.forName("io.github.matirosen.nms."+stringVersion+".NMSImpl");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (NMS.class.isAssignableFrom(Objects.requireNonNull(clazz))){
                try {
                    return (NMS) clazz.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }
}

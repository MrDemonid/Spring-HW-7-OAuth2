package mr.demonid.auth.server.tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class LocalIPAddresses {
    public static Set<String> getAllLocalIPAddresses() {
        Set<String> ipAddresses = new HashSet<>();
        try {
            // Получаем список всех сетевых интерфейсов
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                String name = networkInterface.getName().toLowerCase();
                System.out.println("local[]" + name);
                // Игнорируем выключенные или loopback интерфейсы
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                // Получаем все адреса для интерфейса
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    // Сохраняем только IPv4-адреса
                    if (address.getHostAddress().contains(".")) {
                        System.out.println("  -- " + address.getHostAddress());
                        ipAddresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("Ошибка при получении локальных IP-адресов", e);
        }
        return ipAddresses;
    }

}

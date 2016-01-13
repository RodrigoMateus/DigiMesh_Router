package com.maykot.main;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.digi.xbee.api.DigiMeshDevice;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.maykot.mqtt.MqttRouter;
import com.maykot.radiolibrary.RadioRouter;
import com.maykot.utils.DeviceConfig;
import com.maykot.utils.DiscoverRemoteDevice;
import com.maykot.utils.LogRecord;
import com.maykot.utils.OpenMyDevice;

public class MainApp {

	/* Arquivo de configurações do sistema */
	static DeviceConfig deviceConfig;

	/* XTends */
	public static DigiMeshDevice myDevice;
	public static RemoteXBeeDevice remoteDevice;

	/* MQTT */
	static String BROKER_URL = null;
	static String CLIENT_ID = null;
	static String SUBSCRIBED_TOPIC = null;
	static int QoS = -1;
	static MqttClient mqttClient;

	public static void main(String[] args) {
		System.out.println(" +-------------------+");
		System.out.println(" |  DigiMesh Router  |");
		System.out.println(" +-------------------+\n");

		new LogRecord();

		deviceConfig = DeviceConfig.getInstance();
		myDevice = OpenMyDevice.open(deviceConfig);

		// Registra listener para processar mensagens recebidas
		RadioRouter.getInstance().addProcessMessageListener(new ProcessMessage());

		try {
			remoteDevice = DiscoverRemoteDevice.discover(deviceConfig, myDevice);
		} catch (XBeeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			MqttRouter.getInstance().setMqttRouter(deviceConfig, myDevice, remoteDevice);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Menu para seleção de Testes
		TestRouter.showMenu();
	}

}
/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package org.kuali.common.docker.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerClient;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.command.BuildImgCmd;
import com.sun.jersey.api.client.ClientResponse;

/**
 * @author ocleirig
 *
 */
public class DockerClientMain {

	private static final Logger log = LoggerFactory.getLogger(DockerClientMain.class);
	
	/**
	 * 
	 */
	public DockerClientMain() {
		// TODO Auto-generated constructor stub
	}

	private static void usage () {
		
		System.err.println("USAGE: <docker remote api url> <mode: create-image> ");
		createImageUsage(false);
		System.exit(-1);
	}
	
	private static void createImageUsage (boolean exitJvm) {
		System.err.println("mode: create-image <docker tar file> [<image tag name>]");
		
		if (exitJvm)
			System.exit(-1);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length < 1)
			usage();
		
		String serverUrl = args[0];
		
		try {
			DockerClient client = new DockerClient(serverUrl);

			if (args.length < 2)
				usage();
			
			String mode = args[1];
			
			if (mode.toLowerCase().trim().equals("create-image")) {
				
				if (args.length < 3)
					createImageUsage(true);
				
				String dockerTarArchive = args[2].trim();
				
				BuildImgCmd buildImageCmd = client.buildImageCmd(new FileInputStream (new File (dockerTarArchive)));
				
				if (args.length > 3)
					buildImageCmd.withTag(args[3]);
				
				buildImageCmd.withNoCache(true);
				
				ClientResponse response = buildImageCmd.exec();
				
				if (response.getStatus() != 200)
					System.err.println("FAILED");
			}
		} catch (Exception e) {

			log.error ("failed", e);
		}
		
		
	}

}

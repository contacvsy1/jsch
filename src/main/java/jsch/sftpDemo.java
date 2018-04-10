package jsch;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class sftpDemo {

    public static void main(String[] args) throws JSchException, SftpException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("enter hostname");
        String hostname = br.readLine();

        System.out.println("enter user");
        String user = br.readLine();

        System.out.println("enter pw");
        String password = br.readLine();

        System.out.println("enter directory to list");
        String directory = br.readLine();

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        JSch ssh = new JSch();
        Session session = ssh.getSession(user, hostname, 22);
        session.setConfig(
        	    "PreferredAuthentications", 
        	    "publickey,keyboard-interactive,password");
        session.setConfig(config);
        session.setPassword(password);
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();

        ChannelSftp sftp = (ChannelSftp) channel;sftp.ls("*");
        sftp.cd(directory);
        @SuppressWarnings("unchecked")
		Vector<ChannelSftp.LsEntry> files = sftp.ls("*");
        System.out.printf("Found %d files in dir %s%n", files.size(), directory);

        for (ChannelSftp.LsEntry file : files) {
          //  if (file.getAttrs().isDir()) {
          //      continue;
          //  }
            System.out.printf("Reading file : %s%n", file.getFilename());
            BufferedReader bis = new BufferedReader(new InputStreamReader(sftp.get(file.getFilename())));
            String line = null;
       //     while ((line = bis.readLine()) != null) {
      //          System.out.println(line);
       //     }
            bis.close();
        }

        channel.disconnect();
        session.disconnect();

    }

}
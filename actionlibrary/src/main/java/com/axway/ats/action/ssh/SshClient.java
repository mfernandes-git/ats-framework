/*
 * Copyright 2017 Axway Software
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axway.ats.action.ssh;

import java.io.Closeable;

import com.axway.ats.common.PublicAtsApi;
import com.axway.ats.core.ssh.JschSshClient;

/**
 * An SSH client class
 * 
 * <br/><br/>
 * <b>User guide</b>  
 * <a href="https://techweb.axway.com/confluence/display/ATS/SSH+Operations">page</a>
 * related to this class
 */
@PublicAtsApi
public class SshClient implements Closeable {

    private JschSshClient sshClient;

    /**
     * Construct SSH client. It will work on the default port 22
     * 
     * @param host the target host
     * @param user the user name
     * @param password the user password
     */
    public SshClient( String host,
                      String user,
                      String password ) {

        this( host, user, password, 22 );
    }

    /**
     * Construct SSH client and specify the port to use
     * 
     * @param host the target host
     * @param user the user name
     * @param password the user password
     * @param port the specific port to use
     */
    public SshClient( String host,
                      String user,
                      String password,
                      int port ) {

        sshClient = new JschSshClient();

        sshClient.connect( user, password, host, port );
    }

    /**
     * Disconnect the SSH session connection
     */
    @PublicAtsApi
    @Override
    public void close() {

        sshClient.disconnect();
    }

    /**
     * Starts and a command and waits for its completion
     * 
     * @param command SSH command to execute
     * @return the exit code
     */
    @PublicAtsApi
    public void execute(
                         String command ) {

        sshClient.execute( command, true );
    }

    /**
     * Returns standard output content
     *
     * @return standard output content
     */
    @PublicAtsApi
    public String getStandardOutput() {

        return sshClient.getStandardOutput();
    }

    /**
     * Returns error output content
     *
     * @return error output content
     */
    @PublicAtsApi
    public String getErrorOutput() {

        return sshClient.getErrorOutput();
    }

    /**
     * @return the exit code of the executed command
     */
    @PublicAtsApi
    public int getCommandExitCode() {

        return sshClient.getCommandExitCode();
    }

    /*
     * If user fail to disconnect, we try when the GC collects this instance 
     * (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {

        if( sshClient != null ) {
            sshClient.disconnect();
        }
    }
}

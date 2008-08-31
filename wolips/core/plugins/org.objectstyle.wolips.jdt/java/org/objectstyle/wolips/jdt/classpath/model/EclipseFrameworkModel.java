/* ====================================================================
 * 
 * The ObjectStyle Group Software License, Version 1.0 
 *
 * Copyright (c) 2004 The ObjectStyle Group 
 * and individual authors of the software.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        ObjectStyle Group (http://objectstyle.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "ObjectStyle Group" and "Cayenne" 
 *    must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact andrus@objectstyle.org.
 *
 * 5. Products derived from this software may not be called "ObjectStyle"
 *    nor may "ObjectStyle" appear in their names without prior written
 *    permission of the ObjectStyle Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE OBJECTSTYLE GROUP OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the ObjectStyle Group.  For more
 * information on the ObjectStyle Group, please see
 * <http://objectstyle.org/>.
 *
 */
package org.objectstyle.wolips.jdt.classpath.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.objectstyle.woenvironment.frameworks.FrameworkModel;
import org.objectstyle.woenvironment.frameworks.Root;
import org.objectstyle.wolips.core.resources.types.project.IProjectAdapter;
import org.objectstyle.wolips.variables.VariablesPlugin;

public class EclipseFrameworkModel extends FrameworkModel<IEclipseFramework> {
	private IProject project;

	public EclipseFrameworkModel(IProject project) {
		this.project = project;
	}

	protected synchronized List<Root<IEclipseFramework>> createRoots() {
		List<Root<IEclipseFramework>> roots = new LinkedList<Root<IEclipseFramework>>();
		IProjectAdapter projectAdapter = (IProjectAdapter) this.project.getAdapter(IProjectAdapter.class);
		if (projectAdapter != null) {
			String projectFrameworkFolderPath = projectAdapter.getBuildProperties().getProjectFrameworkFolder();
			if (projectFrameworkFolderPath != null) {
				IFolder projectFrameworkFolder = this.project.getFolder(projectFrameworkFolderPath);
				if (projectFrameworkFolder.exists()) {
					roots.add(new EclipseFolderRoot(Root.PROJECT_LOCAL_ROOT, "Project Local Frameworks", projectFrameworkFolder.getLocation().toFile(), projectFrameworkFolder.getLocation().toFile()));
				}
			}
		}
		roots.add(new EclipseProjectRoot(Root.PROJECT_ROOT, "Project Frameworks", ResourcesPlugin.getWorkspace().getRoot()));

		IPath externalBuildRootPath = VariablesPlugin.getDefault().getExternalBuildRoot();
		IPath externalBuildFrameworkPath = VariablesPlugin.getDefault().getExternalBuildFrameworkPath();
		if (externalBuildRootPath != null && externalBuildFrameworkPath != null) {
			roots.add(new EclipseFolderRoot(Root.EXTERNAL_ROOT, "External Build Root", externalBuildFrameworkPath.toFile(), externalBuildRootPath.toFile()));
		}
		
		IPath userRoot = VariablesPlugin.getDefault().getUserRoot();
		IPath userFrameworkPath = VariablesPlugin.getDefault().getUserFrameworkPath();
		if (userRoot != null && userFrameworkPath != null) {
			roots.add(new EclipseFolderRoot(Root.USER_ROOT, "User Frameworks", userRoot.toFile(), userFrameworkPath.toFile()));
		}
		
		IPath localRoot = VariablesPlugin.getDefault().getLocalRoot();
		IPath localFrameworkPath = VariablesPlugin.getDefault().getLocalFrameworkPatb();
		if (localRoot != null && localFrameworkPath != null) {
			roots.add(new EclipseFolderRoot(Root.LOCAL_ROOT, "Local Frameworks", localRoot.toFile(), localFrameworkPath.toFile()));
		}
		
		IPath systemRoot = VariablesPlugin.getDefault().getSystemRoot();
		IPath systemFrameworkPath = VariablesPlugin.getDefault().getSystemFrameworkPath();
		if (systemRoot != null && systemFrameworkPath != null) {
			roots.add(new EclipseFolderRoot(Root.SYSTEM_ROOT, "System Frameworks", systemRoot.toFile(), systemFrameworkPath.toFile()));
		}
		
		IPath networkRoot = VariablesPlugin.getDefault().getNetworkRoot();
		IPath networkSystemPath = VariablesPlugin.getDefault().getNetworkFrameworkPath();
		if (networkRoot != null && networkSystemPath != null) {
			roots.add(new EclipseFolderRoot(Root.NETWORK_ROOT, "Network Frameworks", networkRoot.toFile(), networkSystemPath.toFile()));
		}
		return roots;
	}
}
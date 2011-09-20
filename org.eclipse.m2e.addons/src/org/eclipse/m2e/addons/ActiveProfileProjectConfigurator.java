package org.eclipse.m2e.addons;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;

/**
 * Project configurator to enable Maven profiles based on a <tt>m2eclipse.activeprofiles</tt> property.
 * 
 * @author Phillip Webb
 */
public class ActiveProfileProjectConfigurator extends AbstractProjectConfigurator {

	@Override
	public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {
		MavenProject mavenProject = request.getMavenProject();
		String activeProfiles = (String) mavenProject.getProperties().get("m2eclipse.activeprofiles");
		if (activeProfiles != null && activeProfiles.trim().length() > 0) {
			ResolverConfiguration configuration = request.getMavenProjectFacade().getResolverConfiguration();
			if (configuration.getActiveProfileList().isEmpty()) {
				configuration.setActiveProfiles(activeProfiles);
			    IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();
			    configurationManager.setResolverConfiguration(request.getProject(), configuration);
			}
		}
	}
}

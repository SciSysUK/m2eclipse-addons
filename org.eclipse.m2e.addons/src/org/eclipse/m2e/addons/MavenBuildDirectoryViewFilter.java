package org.eclipse.m2e.addons;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectRegistry;

/**
 * {@link ViewerFilter} that supports filtering the maven build directory.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class MavenBuildDirectoryViewFilter extends ViewerFilter {

	private static final String MAVEN_NATURE_ID = "org.eclipse.m2e.core.maven2Nature"; //$NON-NLS-1$

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		try {
			if (parentElement instanceof IJavaProject && element instanceof IFolder) {
				return select((IJavaProject) parentElement, (IFolder) element);
			}
		} catch (CoreException e) {
		}
		return true;
	}

	private boolean select(IJavaProject javaProject, IFolder element) throws CoreException {
		IProject project = javaProject.getProject();
		if (project.hasNature(MAVEN_NATURE_ID)) {
			IMavenProjectRegistry registry = MavenPlugin.getMavenProjectRegistry();
			IMavenProjectFacade mavenProjectFacade = registry.getProject(project);
			if (mavenProjectFacade != null) {
				MavenProject mavenProject = mavenProjectFacade.getMavenProject(null);
				Build build = mavenProject.getBuild();
				if (build != null) {
					String directory = build.getDirectory();
					if (directory != null) {
						IFolder targetFolder = project.getFolder(mavenProjectFacade.getProjectRelativePath(directory));
						if (targetFolder.equals(element)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}

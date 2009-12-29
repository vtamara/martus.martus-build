name = 'martus-bc-jce'

define name, :layout=>create_layout_with_source_as_source(name) do
	project.group = 'org.martus'
	project.version = '1'
	jar_file = _('target/#{name}.jar')
	
	compile.options.target = '1.5'
	compile.with(
		'bouncycastle:bcprov-jdk14:jar:135'
	)

	package :jar, :file=>jar_file

	# isn't there an easier way to ask the project for its artifact?
	jar_artifact_id = "#{project.group}:#{name}:jar:#{project.version}"
	install artifact(jar_artifact_id).from(jar_file)

end
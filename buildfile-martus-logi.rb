name = "martus-logi"

define name, :layout=>create_layout_with_source_as_source(name) do
	project.group = 'org.martus'
  project.version = $BUILD_NUMBER

	compile.options.source = '1.5'
	compile.options.target = compile.options.source
	compile.with(
	)
  
	package :jar
	package :sources
end

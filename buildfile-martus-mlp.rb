name = "martus-mlp"

define name, :layout=>create_layout_with_source_as_source(name) do
	project.group = 'org.martus'
	project.version = '1'

	# Each MLP jar needs to be signed
end
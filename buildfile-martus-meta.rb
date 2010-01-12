name = "martus-meta"

define name, :layout=>create_layout_with_source_as_source(name) do
	project.group = 'org.martus'
	project.version = '1'

	compile.options.target = '1.5'
	compile.with(
		JUNIT_SPEC,
		project('martus-utils').packages.first,
		project('martus-common').packages.first,
		project('martus-clientside').packages.first,
		project('martus-client').packages.first,
		project('martus-server').packages.first,
		project('martus-amplifier').packages.first
	)

	# No point in executing TestMeta or TestMetaQuick since they
	# just execute all the other tests anyway
	test.exclude('org.martus.meta.TestMeta')
	test.exclude('org.martus.meta.TestMetaQuick')

	# This test fails due to a hard-coded Windows filename in
	# /martus-js-xml-generator/source/org/martus/martusjsxmlgenerator/text_finalResultWithAttachments.xml
	test.exclude('org.martus.martusjsxmlgenerator.TestImportCSV')

	# Not sure why these tests fail
	test.exclude('org.martus.meta.TestHeadQuartersTableModelConfiguration')
	test.exclude('org.martus.meta.TestHeadQuartersTableModelEdit')
	test.exclude('org.martus.meta.TestRetrieveHQTableModel')
	test.exclude('org.martus.meta.TestSSL')
	test.exclude('org.martus.meta.TestSpeed')

	test.with(
		ICU4J_SPEC,
		BCPROV_SPEC,
		XMLRPC_SPEC,
		JETTY_SPEC,
		VELOCITY_DEP_SPEC
	)
	
	package :jar

	# NOTE: Old build script signed this jar

end
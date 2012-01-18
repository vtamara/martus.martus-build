name = 'martus-client'

def extract_sig_file_to_crypto(jar_artifact, base_filename)
  result = FileUtils.mkdir_p crypto_dir
  puts "Created #{crypto_dir} result=#{result} exists=#{File.exists?(crypto_dir)}"

  sf_file = File.join(main_target_dir, "META-INF/#{base_filename}.SF")
  FileUtils.rm_f sf_file
  unzip_one_entry(jar_artifact, "META-INF/#{base_filename}.SF", main_target_dir)

  sig_file = sig_file(base_filename)
  FileUtils.rm_f sig_file
  FileUtils.move(sf_file, sig_file)
  puts "Moved #{sf_file} (#{File.exists?(sf_file)}) to #{sig_file}"
  return sig_file
end

def main_source_dir
  return _('source', 'main', 'java')
end

def main_target_dir
  return _('target', 'main', 'classes')
end

def test_source_dir
  return _('source', 'test', 'java')
end

def test_target_dir
  return _('target', 'test', 'classes')
end

def crypto_dir
  return _(main_target_dir, 'org', 'martus', 'common', 'crypto')
end

def sig_file(base_filename)
  return File.join(crypto_dir, "#{base_filename}.SIG")
end

def bcjce_sig_file
  return sig_file("SSMTSJAR")
end

def bcprov_sig_file
  return sig_file("BCKEY")
end

define name, :layout=>create_layout_with_source_as_source(name) do
	project.group = 'org.martus'
	project.version = '1'

	compile.options.target = '1.5'
	compile.with(
		JUNIT_SPEC,
		project('martus-utils').packages.first,
		project('martus-common').packages.first,
		project('martus-swing').packages.first,
		project('martus-clientside').packages.first,
		BCPROV_SPEC,
		LAYOUTS_SPEC,
		project('martus-jar-verifier').packages.first,
		VELOCITY_SPEC
	)

	build do
	  version_file = _('target', 'version.txt') 
    FileUtils::mkdir_p(_('target'))
    File.open(version_file, "w") do | file |
      file.puts(Time.now)
    end

    filter(test_source_dir).include('**/test/*.mlp').into(test_target_dir).run
		filter(test_source_dir).include('**/test/Sample*.*').into(test_target_dir).run
		# TODO: Need to exclude unapproved mtf files like km
		filter(test_source_dir).include('**/test/Martus-*.mtf').into(test_target_dir).run
		filter(test_source_dir).include('**/test/MartusHelp-*.txt').into(test_target_dir).run
		filter(test_source_dir).include('**/test/MartusHelpTOC-*.txt').into(test_target_dir).run

		filter(main_source_dir).include('**/*.png').into(main_target_dir).run
		filter(main_source_dir).include('**/*.gif').into(main_target_dir).run
		filter(main_source_dir).include('**/*.jpg').into(main_target_dir).run

		filter(main_source_dir).include('org/martus/client/swingui/Martus-*.mtf').into(main_target_dir).run
		filter(main_source_dir).include('org/martus/client/swingui/MartusHelp-*.txt').into(main_target_dir).run
		filter(main_source_dir).include('org/martus/client/swingui/MartusHelpTOC-*.txt').into(main_target_dir).run

		filter(main_source_dir).include('org/martus/client/swingui/UnofficialTranslationMessage.txt').into(main_target_dir).run
		filter(main_source_dir).include('org/martus/client/swingui/UnofficialTranslationMessageRtoL.txt').into(main_target_dir).run

	end

	test.with(
		ICU4J_SPEC,
		BCPROV_SPEC,
		VELOCITY_DEP_SPEC
	)

	test.exclude('org.martus.client.test.TestImporterOfXmlFilesOfBulletins')
	test.exclude('org.martus.client.test.TestLocalization')
	test.exclude('org.martus.client.test.TestMartusApp_NoServer')

	file bcjce_sig_file => project('martus-thirdparty') do
	  extract_sig_file_to_crypto(artifact(BCJCE_SPEC), "SSMTSJAR")
	end
	
  file bcprov_sig_file => project('martus-thirdparty') do
    extract_sig_file_to_crypto(artifact(BCPROV_SPEC), "BCKEY")
	end

	package(:jar, :file => _('target', "martus-client-unsigned-#{project.version}.jar")).tap do | p |
	  p.with :manifest=>manifest.merge('Main-Class'=>'org.martus.client.swingui.Martus')
    p.include(bcjce_sig_file)
    p.include(bcprov_sig_file)
  
    p.include(File.join(_('source', 'test', 'java'), '**/*.mlp'))
    p.merge(project('martus-jar-verifier').package(:jar))
    p.merge(project('martus-common').package(:jar))
    p.merge(project('martus-utils').package(:jar))
    p.merge(project('martus-hrdag').package(:jar))
    p.merge(project('martus-logi').package(:jar))
    p.merge(project('martus-swing').package(:jar))
    p.merge(project('martus-clientside').package(:jar))
    p.merge(project('martus-js-xml-generator').package(:jar))
	end

  options = {
    :classifier=>'sources', 
    :file => _('target', "martus-client-sources-#{project.version}.zip")}
  }
  package(:zip, options).tap do | p |
    p.include(File.join(_('source', 'test', 'java'), '**/*.mlp'))
    p.merge(project('martus-jar-verifier').package(:sources))
    p.merge(project('martus-common').package(:sources))
    p.merge(project('martus-utils').package(:sources))
    p.merge(project('martus-hrdag').package(:sources))
    p.merge(project('martus-logi').package(:sources))
    p.merge(project('martus-swing').package(:sources))
    p.merge(project('martus-clientside').package(:sources))
    p.merge(project('martus-js-xml-generator').package(:sources))
  end

end

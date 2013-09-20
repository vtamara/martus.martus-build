;The Martus(tm) free, social justice documentation and
;monitoring software. Copyright (C) 2001-2006, Beneficent
;Technology, Inc. (Benetech).

;Martus is free software; you can redistribute it and/or
;modify it under the terms of the GNU General Public License
;as published by the Free Software Foundation; either
;version 2 of the License, or (at your option) any later
;version with the additions and exceptions described in the
;accompanying Martus license file entitled "license.txt".

;It is distributed WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;IMPLIED, including warranties of fitness of purpose or
;merchantability.  See the accompanying Martus License and
;GPL license for more details on the required license terms
;for this software.

;You should have received a copy of the GNU General Public
;License along with this program; if not, write to the Free
;Software Foundation, Inc., 59 Temple Place - Suite 330,
;Boston, MA 02111-1307, USA.

!define LANG "FARSI" ; Required
;!insertmacro LANG_STRING <STRING_DEFINE> "string_value"

; language selection dialog stuff
!insertmacro LANG_STRING LangDialog_Title "����� ������� ��� �����"
!insertmacro LANG_STRING LangDialog_Text "����� ������� ��� ����� �� ������ ����."

!insertmacro LANG_STRING FinishDialog_Text "${PRODUCT_NAME} ${PRODUCT_EXTENDED_VERSION} �� ��� ���?���� ��� ��� ��.\r\n \r\n Visit http://www.martus.org/downloads/ to download Martus Language Packs. \r\n \r\nمجموعه يا «بستهء زبان» (Language Pack)اين امکان را برای شما �?راهم می کندکه پس از آماده شدن و به بازارآمدن�? هرنسخهء جديدتری از Martus، بتوانيد هرزمان که بخواهيد ترجمه های تازه تر و مدارک�? راهنمای جديدتر را نيز نصب کنيد. بسته های زبان، مجموعه های مستقلی که به زبانهای مختل�?�? دنيا آماده شده اند، حاوی�? آخرين نسخهء ترجمهء خود�? برنامهء Martus اند به همراه آخرين نسخه های راهنمای است�?اده، راهنمای �?وری، �?ايل�? مشخصات (README)، و راهنمای کمک در داخل خود�? برنامه. چنانچه در پايين، به 'بستهء زبان �?ارسی' لينک داده شده باشد و اين بسته مطابق با نسخهء برنامهء Martus روی کامپيوتر�? شما باشد، شما می توانيد �?ايلی را که Martus-fa.mlp نام دارد پياده کرده و آن را در �?ولدر يا ديرکتوری�? Martus ذخيره کنيد. از آن پس، وقتی که برنامهء Martus را باز کنيد، اين �?ايل هم بالا خواهد آمد و آخرين نسخهء ترجمه را با/يا راهنماها و اطلاعات�? کمکی�? تازه تردر اختيار شما می گذارد و همزمان مدارک�? تازه تر را نيز در �?ولدر�? مخصوص خودشان Martus\Docs قرار می دهد.\r\n \r\n�� ��� ���� ?���� ���� ���� �� ��� ������ ���� ����."

; shortcuts
!insertmacro LANG_STRING StartMenuShortcutQuestion_Text "��� �� ��� �� ����� ������ Martus ���� ��� ���� �� ����� ����(Start) ��� ?��Ͽ"
!insertmacro LANG_STRING DesktopShortcutQuestion_Text "��� �� ��� �� ����� �����Martus ��� ����� ���?������� ��� ?��Ͽ"
!insertmacro LANG_STRING LaunchProgramInfo_Text "������ Martus �� ������ ������ ���� ��$INSTDIR ��� ��. �� ���� ������ �� �?�� �� ���� �������� Martus ������� ����."

!insertmacro LANG_STRING MartusShortcutDescription_Text "������ ������ ����� ����� ���Martus"

!insertmacro LANG_STRING MartusUserGuideShortcut_Text "������� �������"
!insertmacro LANG_STRING MartusUserGuideShortcut_Filename "martus_user_guide_fa.pdf"

!insertmacro LANG_STRING MartusQuickstartShortcut_Text "�������� ����� ���"
!insertmacro LANG_STRING MartusQuickstartShortcut_Filename "quickstartguide_fa.pdf"

!insertmacro LANG_STRING MartusUninstallShortcut_Text "���� ������"

; file property for .mba
!insertmacro LANG_STRING MartusMBAFileDesc_Text "����� ����� ��� ������ Martus"

; uninstall strings
!insertmacro LANG_STRING UninstallSuccess_Text "$(^Name) �� ��� ���?���� �� ������� ?ǘ ��."

!insertmacro LANG_STRING NeedAdminPrivileges_Text "��� ���� ��� $(^Name) �� ��� ���� �� ������ ������� ��� ��� ���?���� ���� �����."
!insertmacro LANG_STRING NeedAdminPrivilegesError_Text "���� �������� �� ���� ��������� ������. ����� ���� �� ��� ����� ��� ���?���� ����� ?�� �� ��� ������� ��� $(^Name) ������ ����� ���."

!insertmacro LANG_STRING UninstallProgramRunning_Text "���� ����� ���� �� �� $(^Name) ����� ���� ����ϡ �?��� ������� ?ǘ ����� ������ ������ ���� ���� �� �� ǘ��� ��� ����� �� ?ǘ ���."

!insertmacro LANG_STRING NewerVersionInstalled_Text "����� ���� ��� ($EXISTING_MARTUS_VERSION) �� ������� ${PRODUCT_NAME} �� ��� ��� ��� ���. ��� ���� ��� ����� ����� �� ?ǘ ���� �� ������� ��� ����� ����� �� �� ��� ����. ���� ���� �?� �� ����� ���� �� ���� ?���� ���� �� ��ј���� �� �� ��� ������ ��ϡ � ��� ��� ������� ����� ���� �� �� ����� ���� �� ���� ��� ��� �� ������. ���� ��� ����� ���� �� Ϙ��� '�����' �� ��� ���� �� �� ������ ���� ��� �� ���� ��� �?� ��ǘ�� ����� ����� �� �� �� �����ϡ ������ �� ��� ���� ��ј����� ���� ��� �� ������ ���� ���ϡ ��� ������ �� ��� ���ϡ �?� ����� ����� �� �� ��� ����."
!insertmacro LANG_STRING SameVersionInstalled_Text "����� ���� ��� ($EXISTING_MARTUS_VERSION) �� ������� ${PRODUCT_NAME} �� ��� ��� ��� ���. ��� �� ������ ���� ��� ���Ͽ"
!insertmacro LANG_STRING UpgradeVersionInstalled_Text "�����($EXISTING_MARTUS_VERSION) �� ����� �� ��� ��${PRODUCT_NAME} �� ��� ��� ���. ������� ��� ����� ���� �� ����� ������${PRODUCT_EXTENDED_VERSION} ��� ����� ���."
!insertmacro LANG_STRING RemoveInstallShieldVersion_Text "� ����� ����� �� ��${PRODUCT_NAME} ��� ���?���� ��� ����� ���. �� ��� ������ ��� �� ������� �� ������� ��� ����� ���� ?ǘ ���� ?� �� ���� ������� ������ ���� ������� ���� ����� ����� ����. �?� ��� �ǘ��� �� ����� ����� �� ������� ������ Martus ���� ����� ����� ���� ���� ��ϡ �� ?������ �� ���� �� ��� ������ ���� ���� � ����� ����� �� ���� ���� � �?� ��?���� � ������� ���� �� ��� ����. ?� �� ��� ��� ��� ������ �� ������ ������� ��� ����� �� �� ��� �������. ��� ���� �� ������ �� ��� ����� ���Ͽ"
!insertmacro LANG_STRING CannotUpgradeNoJava_Text "���� �� ��Martus �� �� ��� �� ǘ��� ��� ���� ��� ���� ����� �� ����� ������ ��� �� �� ��� ����� ���� ������ ��� ����� �� ����� Java(����) ?���� ��� ����."
!insertmacro LANG_STRING CannotRemoveInstallShieldVersion_Text "�������� �� ��������� ����� ������Martus �� �� ��� ���?����� ��� ��� ����. ������� ��� ����� ���� ���� �� ��ϡ ��� ������ ���� ��� ������� Martus �� ���� Add/Remove(�����/���) �� Control Panel(����� �����) ����� � ������ �� �� ���� ��� ���ϡ ��� ������ �� ��� ������� ��� ����� ��?����. �?� ���� ����� ����� �� ������� �� �� ����� ����Martus ���� ���� ��� ?������ �� ���� �� ǘ��� ��� ��� �� ?�� �� ?ǘ ���� ������ Ș���."
!insertmacro LANG_STRING CannotUpgradeNoMartus_Text "��� �������� ����� ������� Martus ���. ����� �� ���� ����� ����� ������� ��� ����� (installer) �� �� ���� ������� ���� (Java) ��� �� ?���� � ��� ����."


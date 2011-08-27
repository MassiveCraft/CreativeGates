package com.massivecraft.creativegates.zcore.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.DyeColor;
import org.bukkit.TreeSpecies;

public class TextUtil
{
	private Map<String, String> tags = new HashMap<String, String>();
	private Map<String, String> lang = new HashMap<String, String>();
	
	public TextUtil(Map<String, String> tags, Map<String, String> lang)
	{
		if (tags != null)
		{
			this.tags.putAll(tags);
		}
		
		if (lang != null)
		{
			this.lang.putAll(lang);
		}
	}
	
	public String get(String name)
	{
		String str = lang.get(name);
		if (str == null) return name;
		return this.tags(str);
	}
	
	public String get(String name, Object... args)
	{
		String str = lang.get(name);
		if (str == null) return name;
		return String.format(this.tags(str), args);
	}
	
	public Map<String, String> getTags()
	{
		return tags;
	}

	public Map<String, String> getLang()
	{
		return lang;
	}
	
	public String titleize(String str)
	{
		String line = repeat("_", 60);
		String center = ".[ "+ tags("<l>") + str + tags("<a>")+ " ].";
		int pivot = line.length() / 2;
		int eatLeft = center.length() / 2;
		int eatRight = center.length() - eatLeft;

		if (eatLeft < pivot)
			return tags("<a>")+line.substring(0, pivot - eatLeft) + center + line.substring(pivot + eatRight);
		else
			return tags("<a>")+center;
	}
	
	public String tags(String str)
	{
		return replaceTags(str, this.tags);
	}
	
	public static final transient Pattern patternTag = Pattern.compile("<([^<]*)>");
	public static String replaceTags(String str, Map<String, String> tags)
	{
		StringBuffer ret = new StringBuffer();
		Matcher matcher = patternTag.matcher(str);
		while (matcher.find())
		{
			String tag = matcher.group(1);
			String repl = tags.get(tag);
			if (repl == null)
			{
				matcher.appendReplacement(ret, "<"+tag+">");
			}
			else
			{
				matcher.appendReplacement(ret, repl);
			}
		}
		matcher.appendTail(ret);
		return ret.toString();
	}
	
	public static String implode(List<String> list, String glue)
	{
	    StringBuilder ret = new StringBuilder();
	    for (int i=0; i<list.size(); i++)
	    {
	        if (i!=0)
	        {
	        	ret.append(glue);
	        }
	        ret.append(list.get(i));
	    }
	    return ret.toString();
	}
	
	public static String repeat(String s, int times)
	{
	    if (times <= 0) return "";
	    else return s + repeat(s, times-1);
	}
	
	public static String getMaterialName(Material material)
	{
		return material.toString().replace('_', ' ').toLowerCase();
	}
	
	public static String getMaterialName(int materialId)
	{
		return getMaterialName(Material.getMaterial(materialId));
	}
	
	public static String upperCaseFirst(String string)
	{
		return string.substring(0, 1).toUpperCase()+string.substring(1);
	}
	public static String parseByData(Material material, byte data)
	{
		String name = "";
		if(material == Material.WOOL)
			name = DyeColor.getByData(data).name();
		if(material == Material.LOG)
			name = TreeSpecies.getByData(data).name() == "GENERIC" ? "" : TreeSpecies.getByData(data).name();
		//Add more if you figure out any more block types
		if(! name.equals(""))
			name = upperCaseFirst(name.replace('_', ' ').toLowerCase()) + " ";
		return name;
	}
}

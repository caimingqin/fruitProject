package com.mce.core.inject.annotation.asm;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.asm.commons.EmptyVisitor;
import org.springframework.util.ClassUtils;

import com.mce.core.inject.ClassMetadata;

public class ClassMetadataReadingVisitor
  implements ClassVisitor, ClassMetadata
{//TODO
  private String className;
  private boolean isInterface;
  private boolean isAbstract;
  private boolean isFinal;
  private String enclosingClassName;
  private boolean independentInnerClass;
  private String superClassName;
  private String[] interfaces;
  private Set<String> memberClassNames = new LinkedHashSet<String>();

  private Set<String> annotationSet = new HashSet<String>();

  public void visit(int version, int access, String name, String signature, String supername, String[] interfaces)
  {
    this.className = ClassUtils.convertResourcePathToClassName(name);
    this.isInterface = ((access & 0x200) != 0);
    this.isAbstract = ((access & 0x400) != 0);
    this.isFinal = ((access & 0x10) != 0);
    if (supername != null) {
      this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
    }
    this.interfaces = new String[interfaces.length];
    for (int i = 0; i < interfaces.length; i++)
      this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
  }

  public void visitOuterClass(String owner, String name, String desc)
  {
    this.enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
  }

  public void visitInnerClass(String name, String outerName, String innerName, int access) {
    if (outerName != null) {
      String fqName = ClassUtils.convertResourcePathToClassName(name);
      String fqOuterName = ClassUtils.convertResourcePathToClassName(outerName);
      if (this.className.equals(fqName)) {
        this.enclosingClassName = fqOuterName;
        this.independentInnerClass = ((access & 0x8) != 0);
      }
      else if (this.className.equals(fqOuterName)) {
        this.memberClassNames.add(fqName);
      }
    }
  }

  public void visitSource(String source, String debug)
  {
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible)
  {
    String className = Type.getType(desc).getClassName();
    this.annotationSet.add(className);

    return new EmptyVisitor();
  }

  public void visitAttribute(Attribute attr)
  {
  }

  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    return new EmptyVisitor();
  }

  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    return new EmptyVisitor();
  }

  public void visitEnd()
  {
  }

  public String getClassName()
  {
    return this.className;
  }

  public boolean isInterface() {
    return this.isInterface;
  }

  public boolean isAbstract() {
    return this.isAbstract;
  }

  public boolean isConcrete() {
    return (!this.isInterface) && (!this.isAbstract);
  }

  public boolean isFinal() {
    return this.isFinal;
  }

  public boolean isIndependent() {
    return (this.enclosingClassName == null) || (this.independentInnerClass);
  }

  public boolean hasEnclosingClass() {
    return this.enclosingClassName != null;
  }

  public String getEnclosingClassName() {
    return this.enclosingClassName;
  }

  public boolean hasSuperClass() {
    return this.superClassName != null;
  }

  public String getSuperClassName() {
    return this.superClassName;
  }

  public String[] getInterfaceNames() {
    return this.interfaces;
  }

  public String[] getMemberClassNames() {
    return (String[])this.memberClassNames.toArray(new String[this.memberClassNames.size()]);
  }

  public String[] getAnnotations()
  {
    return (String[])this.annotationSet.toArray(new String[this.annotationSet.size()]);
  }
}








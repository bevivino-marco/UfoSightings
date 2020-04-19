package it.polito.tdp.ufo.model;

public class Annata {
private int anno;
private int cont;
public Annata(int anno, int cont) {
	super();
	this.anno = anno;
	this.cont = cont;
}
public int getAnno() {
	return anno;
}
public void setAnno(int anno) {
	this.anno = anno;
}
public int getCont() {
	return cont;
}
public void setCont(int cont) {
	this.cont = cont;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + anno;
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Annata other = (Annata) obj;
	if (anno != other.anno)
		return false;
	return true;
}
@Override
public String toString() {
	return String.format("%s, %s", anno, cont);
}


}

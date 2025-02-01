package engineering.pattern.abstract_factory;
/**Definisce un insieme fisso e immutabile di opzioni.
 * Migliora la leggibilità e previene errori rispetto all'uso di stringhe o interi.
 * Permette un codice più sicuro e manutenibile.
 * */
public enum Enum {
    JSON, MYSQL, DEMO;
}